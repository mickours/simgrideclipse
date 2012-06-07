package org.example.emfgmf.topicmap.diagram.part;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.example.emfgmf.topicmap.TopicmapPackage;

/**
 * @generated
 */
public class TopicMapDomainModelElementTester extends PropertyTester {

	/**
	 * @generated
	 */
	public boolean test(Object receiver, String method, Object[] args,
			Object expectedValue) {
		if (false == receiver instanceof EObject) {
			return false;
		}
		EObject eObject = (EObject) receiver;
		EClass eClass = eObject.eClass();
		if (eClass == TopicmapPackage.eINSTANCE.getTopic()) {
			return true;
		}
		if (eClass == TopicmapPackage.eINSTANCE.getAssociation()) {
			return true;
		}
		if (eClass == TopicmapPackage.eINSTANCE.getTopicMap()) {
			return true;
		}
		return false;
	}

}
