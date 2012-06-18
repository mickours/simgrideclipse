package simgrideclipseplugin.editors.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ElementPropertySource implements IPropertySource {
	
	/**
	 * 
	 */
	private final IPropertySource propertySource;

	/**
	 * 
	 * 
	 * @param propertySource
	 */
	public ElementPropertySource(IPropertySource propertySource) {
		this.propertySource = propertySource;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Object getEditableValue() {
		return propertySource.getEditableValue();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		IPropertyDescriptor[] wrappedDescriptors = propertySource
				.getPropertyDescriptors();
		if (wrappedDescriptors != null) {
			for (IPropertyDescriptor pd : wrappedDescriptors) {
				PropertyDescriptor newPd = new PropertyDescriptor(pd.getId(),pd.getDisplayName());
				newPd.setCategory(pd.getCategory());
				newPd.setDescription(pd.getDescription());
				descriptors.add(newPd);
			}
		}
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	/**
	 * 
	 * 
	 * @param id
	 * 
	 * @return
	 */
	public Object getPropertyValue(Object id) {
		return propertySource.getPropertyValue(id);
	}

	/**
	 * 
	 * 
	 * @param id
	 * 
	 * @return
	 */
	public boolean isPropertySet(Object id) {
		return propertySource.isPropertySet(id);
	}

	/**
	 * 
	 * 
	 * @param id
	 */
	public void resetPropertyValue(Object id) {
		propertySource.resetPropertyValue(id);
	}

	/**
	 * 
	 * 
	 * @param value
	 * @param id
	 */
	public void setPropertyValue(Object id, Object value) {
		// do nothing
	}
}
