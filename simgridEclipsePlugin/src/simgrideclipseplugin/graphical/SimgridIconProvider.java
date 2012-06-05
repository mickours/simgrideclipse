package simgrideclipseplugin.graphical;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class SimgridIconProvider {
	private static final String iconPath = "platform:/plugin/simgridEclipsePlugin/icons/";
	private static final HashMap<String,ImageDescriptor> iconRegister = init();
	
	public static Image getIcon(String key){
		Image i = getIconImageDescriptor(key).createImage();
		return i;
	}
	
	public static ImageDescriptor getIconImageDescriptor(String key){
		ImageDescriptor d = iconRegister.get(key);
		if (d == null){
			try {
				throw new Exception("Icon not found");
			} catch (Exception e) {
				d = PlatformUI.getWorkbench().getSharedImages().
						getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK);
			}
		}
		return d;
	}
	
	/**
	 * this is the place where you can add icons
	 * @return a map between file name and logic name
	 */
	private static HashMap<String, String> getPathMap() {
		HashMap<String,String> h = new HashMap<String, String>();
		h.put("AS.small", "AS_small.png");
		h.put("AS.large", "AS_large.png");
		h.put("AS", "AS.png");
		return h;
	}

	private static HashMap<String, ImageDescriptor> init() {
		HashMap<String, ImageDescriptor> h = new HashMap<String,ImageDescriptor>();
		
		URL url = null;
		for(Entry<String, String> e : getPathMap().entrySet()){
			try {
				url = new URL(iconPath+e.getValue());
				h.put(e.getKey(),ImageDescriptor.createFromURL(url));
				
			} catch (MalformedURLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		return h;
	}

}
