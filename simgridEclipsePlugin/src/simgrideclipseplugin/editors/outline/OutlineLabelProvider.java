package simgrideclipseplugin.editors.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Element;


public class OutlineLabelProvider implements ILabelProvider
{

	public OutlineLabelProvider()
	{
		super();
	}

	public Image getImage(Object element)
	{
		return null;
	}

	public String getText(Object elem)
	{
		if (elem instanceof Element)
		{
			Element element = (Element) elem;
			String textToShow = element.toString();
//
//			String nameAttribute = element.getAttributeValue("name");
//			if (nameAttribute != null)
//				textToShow += " " + nameAttribute;

			return textToShow;
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener)
	{
	}

	public void dispose()
	{
	}

	public boolean isLabelProperty(Object element, String property)
	{
		return false;
	}

	public void removeListener(ILabelProviderListener listener)
	{
	}

}