package simgrideclipseplugin.graphical;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import simgrideclipseplugin.graphical.actions.AutoLayoutAction;
import simgrideclipseplugin.model.ElementList;

//FIXME : Modify this this class and use Activator instead
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
			d = PlatformUI.getWorkbench().getSharedImages().
					getImageDescriptor(ISharedImages.IMG_DEF_VIEW);
		}
		return d;
	}
	
	/**
	 * this is the place where you can add icons
	 * @return a map between file name and logic name
	 */
	private static HashMap<String, String> getPathMap() {
		HashMap<String,String> h = new HashMap<String, String>();
		//FIXME must be populate with ElementList.tagList
		h.put(ElementList.AS+".small", "AS_small.png");
		h.put(ElementList.AS+".large", "AS_large.png");
		h.put(ElementList.AS, "AS.svg");

		h.put("host.small", "host_small.svg");
		h.put("host.large", "host.svg");
		h.put("host", "host.svg");
		
		h.put(AutoLayoutAction.ID, "auto_layout.png");

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
