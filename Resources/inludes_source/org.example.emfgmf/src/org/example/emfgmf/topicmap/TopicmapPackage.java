/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.example.emfgmf.topicmap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.example.emfgmf.topicmap.TopicmapFactory
 * @model kind="package"
 * @generated
 */
public interface TopicmapPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "topicmap";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/emfgmf";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.example.emfgmf";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TopicmapPackage eINSTANCE = org.example.emfgmf.topicmap.impl.TopicmapPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.example.emfgmf.topicmap.impl.TopicImpl <em>Topic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.example.emfgmf.topicmap.impl.TopicImpl
	 * @see org.example.emfgmf.topicmap.impl.TopicmapPackageImpl#getTopic()
	 * @generated
	 */
	int TOPIC = 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOPIC__LABEL = 0;

	/**
	 * The number of structural features of the '<em>Topic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOPIC_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.example.emfgmf.topicmap.impl.AssociationImpl <em>Association</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.example.emfgmf.topicmap.impl.AssociationImpl
	 * @see org.example.emfgmf.topicmap.impl.TopicmapPackageImpl#getAssociation()
	 * @generated
	 */
	int ASSOCIATION = 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__LABEL = 0;

	/**
	 * The feature id for the '<em><b>Left</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__LEFT = 1;

	/**
	 * The feature id for the '<em><b>Right</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__RIGHT = 2;

	/**
	 * The number of structural features of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.example.emfgmf.topicmap.impl.TopicMapImpl <em>Topic Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.example.emfgmf.topicmap.impl.TopicMapImpl
	 * @see org.example.emfgmf.topicmap.impl.TopicmapPackageImpl#getTopicMap()
	 * @generated
	 */
	int TOPIC_MAP = 2;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOPIC_MAP__LABEL = 0;

	/**
	 * The feature id for the '<em><b>Topics</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOPIC_MAP__TOPICS = 1;

	/**
	 * The feature id for the '<em><b>Associations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOPIC_MAP__ASSOCIATIONS = 2;

	/**
	 * The number of structural features of the '<em>Topic Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOPIC_MAP_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.example.emfgmf.topicmap.Topic <em>Topic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Topic</em>'.
	 * @see org.example.emfgmf.topicmap.Topic
	 * @generated
	 */
	EClass getTopic();

	/**
	 * Returns the meta object for the attribute '{@link org.example.emfgmf.topicmap.Topic#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.example.emfgmf.topicmap.Topic#getLabel()
	 * @see #getTopic()
	 * @generated
	 */
	EAttribute getTopic_Label();

	/**
	 * Returns the meta object for class '{@link org.example.emfgmf.topicmap.Association <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Association</em>'.
	 * @see org.example.emfgmf.topicmap.Association
	 * @generated
	 */
	EClass getAssociation();

	/**
	 * Returns the meta object for the attribute '{@link org.example.emfgmf.topicmap.Association#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.example.emfgmf.topicmap.Association#getLabel()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_Label();

	/**
	 * Returns the meta object for the reference '{@link org.example.emfgmf.topicmap.Association#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left</em>'.
	 * @see org.example.emfgmf.topicmap.Association#getLeft()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_Left();

	/**
	 * Returns the meta object for the reference '{@link org.example.emfgmf.topicmap.Association#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right</em>'.
	 * @see org.example.emfgmf.topicmap.Association#getRight()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_Right();

	/**
	 * Returns the meta object for class '{@link org.example.emfgmf.topicmap.TopicMap <em>Topic Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Topic Map</em>'.
	 * @see org.example.emfgmf.topicmap.TopicMap
	 * @generated
	 */
	EClass getTopicMap();

	/**
	 * Returns the meta object for the attribute '{@link org.example.emfgmf.topicmap.TopicMap#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.example.emfgmf.topicmap.TopicMap#getLabel()
	 * @see #getTopicMap()
	 * @generated
	 */
	EAttribute getTopicMap_Label();

	/**
	 * Returns the meta object for the containment reference list '{@link org.example.emfgmf.topicmap.TopicMap#getTopics <em>Topics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Topics</em>'.
	 * @see org.example.emfgmf.topicmap.TopicMap#getTopics()
	 * @see #getTopicMap()
	 * @generated
	 */
	EReference getTopicMap_Topics();

	/**
	 * Returns the meta object for the containment reference list '{@link org.example.emfgmf.topicmap.TopicMap#getAssociations <em>Associations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Associations</em>'.
	 * @see org.example.emfgmf.topicmap.TopicMap#getAssociations()
	 * @see #getTopicMap()
	 * @generated
	 */
	EReference getTopicMap_Associations();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TopicmapFactory getTopicmapFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.example.emfgmf.topicmap.impl.TopicImpl <em>Topic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.example.emfgmf.topicmap.impl.TopicImpl
		 * @see org.example.emfgmf.topicmap.impl.TopicmapPackageImpl#getTopic()
		 * @generated
		 */
		EClass TOPIC = eINSTANCE.getTopic();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOPIC__LABEL = eINSTANCE.getTopic_Label();

		/**
		 * The meta object literal for the '{@link org.example.emfgmf.topicmap.impl.AssociationImpl <em>Association</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.example.emfgmf.topicmap.impl.AssociationImpl
		 * @see org.example.emfgmf.topicmap.impl.TopicmapPackageImpl#getAssociation()
		 * @generated
		 */
		EClass ASSOCIATION = eINSTANCE.getAssociation();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSOCIATION__LABEL = eINSTANCE.getAssociation_Label();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION__LEFT = eINSTANCE.getAssociation_Left();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION__RIGHT = eINSTANCE.getAssociation_Right();

		/**
		 * The meta object literal for the '{@link org.example.emfgmf.topicmap.impl.TopicMapImpl <em>Topic Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.example.emfgmf.topicmap.impl.TopicMapImpl
		 * @see org.example.emfgmf.topicmap.impl.TopicmapPackageImpl#getTopicMap()
		 * @generated
		 */
		EClass TOPIC_MAP = eINSTANCE.getTopicMap();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOPIC_MAP__LABEL = eINSTANCE.getTopicMap_Label();

		/**
		 * The meta object literal for the '<em><b>Topics</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TOPIC_MAP__TOPICS = eINSTANCE.getTopicMap_Topics();

		/**
		 * The meta object literal for the '<em><b>Associations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TOPIC_MAP__ASSOCIATIONS = eINSTANCE.getTopicMap_Associations();

	}

} //TopicmapPackage
