package simgrideclipseplugin.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import simgrideclipseplugin.Activator;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDParser;

public class DtdParser {

	public static final DtdParser INSTANCE = new DtdParser();

	private static final String dtdURL = "http://simgrid.gforge.inria.fr/simgrid.dtd";
	private static final String dtdStaticPath = "/model/simgrid.dtd";
	private static final String dtdTmpPath = "./simgrid.dtd";

	private DTDParser parser;
	private DTD dtd;

	public List<String> getAttributesList(String tagName) {
		DTDElement e = (DTDElement) dtd.elements.get(tagName);
		LinkedList<String> attrList = new LinkedList<String>();
		for (Object attr : e.attributes.keySet()) {
			attrList.addFirst((String) attr);
		}
		return attrList;
	}

	public boolean isRequieredField(String tagName, String fieldName) {
		DTDElement e = (DTDElement) dtd.elements.get(tagName);
		return e.getAttribute(fieldName).getDecl().equals(DTDDecl.REQUIRED);
	}

	public String getDefaultValue(String tagName, String fieldName) {
		DTDElement e = (DTDElement) dtd.elements.get(tagName);
		return e.getAttribute(fieldName).defaultValue;
	}

	public List<String> getValueList(String tagName, String fieldName) {
		DTDElement e = (DTDElement) dtd.elements.get(tagName);
		Object type = e.getAttribute(fieldName).getType();
		if (type instanceof DTDEnumeration) {
			DTDEnumeration en = (DTDEnumeration) type;
			return Arrays.asList(en.getItems());
		}
		return Collections.emptyList();
	}

	/* PRIVATE */

	/**
	 * try to get the DTD from the <code>dtdURL</code>. If it does not work,
	 * get the the one in <code>dtdStaticPath</code> instead.
	 */
	private DtdParser() {
		URL dtdUri = null;
		try {
			if (getFile(dtdURL, dtdTmpPath)){
				parser = new DTDParser(new FileReader(dtdTmpPath));
			}else{
				dtdUri = FileLocator.toFileURL(Platform.getBundle(
						Activator.PLUGIN_ID).getEntry(dtdStaticPath));
				File f = new File(dtdUri.toURI());
				parser = new DTDParser(new FileReader(f));
			}
			dtd = parser.parse();
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing the dtd file: " +
					"you need an internet connection\n"+e.getMessage());
		}

	}

	private boolean getFile(String host, String fileName) {
		InputStream input = null;
		FileOutputStream writeFile = null;
		boolean OK = true;
		try {
			URL url = new URL(host);
			URLConnection connection = url.openConnection();
			int fileLength = connection.getContentLength();

			if (fileLength == -1)
				throw new IOException();

			input = connection.getInputStream();
			writeFile = new FileOutputStream(fileName);
			byte[] buffer = new byte[1024];
			int read;

			while ((read = input.read(buffer)) > 0)
				writeFile.write(buffer, 0, read);
			writeFile.flush();
		} catch (IOException e){
			OK = false;
		} finally {
			File f = new File(fileName);
			f.deleteOnExit();
			try {
				writeFile.close();
				input.close();
			} catch (Exception e) {
				//do nothing
			}
		}
		return OK;
	}

}
