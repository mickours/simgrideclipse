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

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDParser;

public class DtdParser {

	
	
	public static final DtdParser INSTANCE = new DtdParser();
	
	private static final String dtdURL = "http://simgrid.gforge.inria.fr/simgrid.dtd";
	private static final String dtdPath = "./simgrid.dtd";
	private DTDParser parser;
	private DTD dtd;
	
	public List<String> getAttributesList(String tagName){
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
		if (type instanceof DTDEnumeration){
			DTDEnumeration en = (DTDEnumeration)type;
			return Arrays.asList(en.getItems());
		}
		return Collections.emptyList();
	}

	/** PRIVATE **/
	
	private DtdParser(){
		try {
			getFile(dtdURL, dtdPath);
			parser = new DTDParser(new FileReader(dtdPath));
			dtd = parser.parse();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private void getFile(String host, String fileName) throws IOException
    {
        InputStream input = null;
        FileOutputStream writeFile = null;

        try
        {
            URL url = new URL(host);
            URLConnection connection = url.openConnection();
            int fileLength = connection.getContentLength();

            if (fileLength == -1) throw new IOException("invalid URL");

            input = connection.getInputStream();
            writeFile = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int read;

            while ((read = input.read(buffer)) > 0)
                writeFile.write(buffer, 0, read);
            writeFile.flush();
        }
        finally
        {
            try
            {
    			File f = new File(dtdPath);
    			f.deleteOnExit();
                writeFile.close();
                input.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
