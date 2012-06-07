/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.example.emfgmf.topicmap;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topic Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.example.emfgmf.topicmap.TopicMap#getLabel <em>Label</em>}</li>
 *   <li>{@link org.example.emfgmf.topicmap.TopicMap#getTopics <em>Topics</em>}</li>
 *   <li>{@link org.example.emfgmf.topicmap.TopicMap#getAssociations <em>Associations</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.example.emfgmf.topicmap.TopicmapPackage#getTopicMap()
 * @model
 * @generated
 */
public interface TopicMap extends EObject {
	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.example.emfgmf.topicmap.TopicmapPackage#getTopicMap_Label()
	 * @model required="true"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.example.emfgmf.topicmap.TopicMap#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Topics</b></em>' containment reference list.
	 * The list contents are of type {@link org.example.emfgmf.topicmap.Topic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Topics</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Topics</em>' containment reference list.
	 * @see org.example.emfgmf.topicmap.TopicmapPackage#getTopicMap_Topics()
	 * @model containment="true"
	 * @generated
	 */
	EList<Topic> getTopics();

	/**
	 * Returns the value of the '<em><b>Associations</b></em>' containment reference list.
	 * The list contents are of type {@link org.example.emfgmf.topicmap.Association}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Associations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Associations</em>' containment reference list.
	 * @see org.example.emfgmf.topicmap.TopicmapPackage#getTopicMap_Associations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Association> getAssociations();

} // TopicMap
