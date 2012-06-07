/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.example.emfgmf.topicmap.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.example.emfgmf.topicmap.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TopicmapFactoryImpl extends EFactoryImpl implements TopicmapFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TopicmapFactory init() {
		try {
			TopicmapFactory theTopicmapFactory = (TopicmapFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.example.org/emfgmf"); 
			if (theTopicmapFactory != null) {
				return theTopicmapFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TopicmapFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopicmapFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TopicmapPackage.TOPIC: return createTopic();
			case TopicmapPackage.ASSOCIATION: return createAssociation();
			case TopicmapPackage.TOPIC_MAP: return createTopicMap();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Topic createTopic() {
		TopicImpl topic = new TopicImpl();
		return topic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Association createAssociation() {
		AssociationImpl association = new AssociationImpl();
		return association;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopicMap createTopicMap() {
		TopicMapImpl topicMap = new TopicMapImpl();
		return topicMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopicmapPackage getTopicmapPackage() {
		return (TopicmapPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TopicmapPackage getPackage() {
		return TopicmapPackage.eINSTANCE;
	}

} //TopicmapFactoryImpl
