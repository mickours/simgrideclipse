package org.example.emfgmf.topicmap.presentation;

import java.util.Collections;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

/**
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ReverseAdapterFactoryContentProvider extends AdapterFactoryContentProvider {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReverseAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object [] getElements(Object object) {
		Object parent = super.getParent(object);
		return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object [] getChildren(Object object) {
		Object parent = super.getParent(object);
		return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean hasChildren(Object object) {
		Object parent = super.getParent(object);
		return parent != null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getParent(Object object) {
		return null;
	}
}