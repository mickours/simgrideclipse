/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.example.emfgmf.topicmap.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.example.emfgmf.topicmap.Association;
import org.example.emfgmf.topicmap.Topic;
import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.TopicmapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topic Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.example.emfgmf.topicmap.impl.TopicMapImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.example.emfgmf.topicmap.impl.TopicMapImpl#getTopics <em>Topics</em>}</li>
 *   <li>{@link org.example.emfgmf.topicmap.impl.TopicMapImpl#getAssociations <em>Associations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TopicMapImpl extends EObjectImpl implements TopicMap {
	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTopics() <em>Topics</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTopics()
	 * @generated
	 * @ordered
	 */
	protected EList<Topic> topics;

	/**
	 * The cached value of the '{@link #getAssociations() <em>Associations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssociations()
	 * @generated
	 * @ordered
	 */
	protected EList<Association> associations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TopicMapImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TopicmapPackage.Literals.TOPIC_MAP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TopicmapPackage.TOPIC_MAP__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Topic> getTopics() {
		if (topics == null) {
			topics = new EObjectContainmentEList<Topic>(Topic.class, this, TopicmapPackage.TOPIC_MAP__TOPICS);
		}
		return topics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Association> getAssociations() {
		if (associations == null) {
			associations = new EObjectContainmentEList<Association>(Association.class, this, TopicmapPackage.TOPIC_MAP__ASSOCIATIONS);
		}
		return associations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TopicmapPackage.TOPIC_MAP__TOPICS:
				return ((InternalEList<?>)getTopics()).basicRemove(otherEnd, msgs);
			case TopicmapPackage.TOPIC_MAP__ASSOCIATIONS:
				return ((InternalEList<?>)getAssociations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TopicmapPackage.TOPIC_MAP__LABEL:
				return getLabel();
			case TopicmapPackage.TOPIC_MAP__TOPICS:
				return getTopics();
			case TopicmapPackage.TOPIC_MAP__ASSOCIATIONS:
				return getAssociations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TopicmapPackage.TOPIC_MAP__LABEL:
				setLabel((String)newValue);
				return;
			case TopicmapPackage.TOPIC_MAP__TOPICS:
				getTopics().clear();
				getTopics().addAll((Collection<? extends Topic>)newValue);
				return;
			case TopicmapPackage.TOPIC_MAP__ASSOCIATIONS:
				getAssociations().clear();
				getAssociations().addAll((Collection<? extends Association>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TopicmapPackage.TOPIC_MAP__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case TopicmapPackage.TOPIC_MAP__TOPICS:
				getTopics().clear();
				return;
			case TopicmapPackage.TOPIC_MAP__ASSOCIATIONS:
				getAssociations().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TopicmapPackage.TOPIC_MAP__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case TopicmapPackage.TOPIC_MAP__TOPICS:
				return topics != null && !topics.isEmpty();
			case TopicmapPackage.TOPIC_MAP__ASSOCIATIONS:
				return associations != null && !associations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (label: ");
		result.append(label);
		result.append(')');
		return result.toString();
	}

} //TopicMapImpl
