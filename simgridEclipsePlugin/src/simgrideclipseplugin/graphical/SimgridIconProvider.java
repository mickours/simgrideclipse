package simgrideclipseplugin.graphical;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import simgrideclipseplugin.graphical.actions.AutoLayoutAction;
import simgrideclipseplugin.model.ElementList;

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
		
		//add Element Icons
		for (String tag : ElementList.getElementTagNameList()){
			h.put(tag+"_small", tag+"_small.svg");
			h.put(tag, tag+".svg");
		}
		
		//add Actions Icons
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
				System.err.println("this icon path : "+iconPath+e.getValue()+"doesn't exists");
			}
		}
		return h;
	}

}
