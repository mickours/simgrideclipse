package simgrideclipseplugin.graphical.providers;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import simgrideclipseplugin.Activator;
import simgrideclipseplugin.graphical.actions.AutoLayoutAction;
import simgrideclipseplugin.graphical.actions.GoIntoAction;
import simgrideclipseplugin.graphical.actions.GoOutAction;
import simgrideclipseplugin.model.ElementList;

public class SimgridIconProvider {
	private static final String iconPath = "icons/";
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
			h.put(tag+"_small", tag+"_small.png");
			h.put(tag, tag+".png");
		}
		
		//add Actions Icons
		h.put(AutoLayoutAction.ID, "auto_layout.png");
		h.put(GoIntoAction.ID, "go-down.png");
		h.put(GoOutAction.ID, "go-up.png");
		//add warning and error
		h.put("error", "error.png");
		h.put("warning", "warning.png");
		
		return h;
	}

	private static HashMap<String, ImageDescriptor> init() {
		HashMap<String, ImageDescriptor> h = new HashMap<String,ImageDescriptor>();
		
		String url = null;
		for(Entry<String, String> e : getPathMap().entrySet()){
			url = iconPath+e.getValue();
			ImageDescriptor desImg = Activator.getImageDescriptor(url);
			if (desImg != null){
				h.put(e.getKey(),desImg);
			}
			else{
				System.out.println("this icon path : "+iconPath+e.getValue()+" doesn't exists");
			}
		}
		return h;
	}

}
