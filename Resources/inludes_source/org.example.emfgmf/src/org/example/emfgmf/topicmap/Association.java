/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.example.emfgmf.topicmap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Association</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.example.emfgmf.topicmap.Association#getLabel <em>Label</em>}</li>
 *   <li>{@link org.example.emfgmf.topicmap.Association#getLeft <em>Left</em>}</li>
 *   <li>{@link org.example.emfgmf.topicmap.Association#getRight <em>Right</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.example.emfgmf.topicmap.TopicmapPackage#getAssociation()
 * @model
 * @generated
 */
public interface Association extends EObject {
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
	 * @see org.example.emfgmf.topicmap.TopicmapPackage#getAssociation_Label()
	 * @model required="true"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.example.emfgmf.topicmap.Association#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Left</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left</em>' reference.
	 * @see #setLeft(Topic)
	 * @see org.example.emfgmf.topicmap.TopicmapPackage#getAssociation_Left()
	 * @model resolveProxies="false" required="true"
	 * @generated
	 */
	Topic getLeft();

	/**
	 * Sets the value of the '{@link org.example.emfgmf.topicmap.Association#getLeft <em>Left</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left</em>' reference.
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(Topic value);

	/**
	 * Returns the value of the '<em><b>Right</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right</em>' reference.
	 * @see #setRight(Topic)
	 * @see org.example.emfgmf.topicmap.TopicmapPackage#getAssociation_Right()
	 * @model resolveProxies="false" required="true"
	 * @generated
	 */
	Topic getRight();

	/**
	 * Sets the value of the '{@link org.example.emfgmf.topicmap.Association#getRight <em>Right</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right</em>' reference.
	 * @see #getRight()
	 * @generated
	 */
	void setRight(Topic value);

} // Association
