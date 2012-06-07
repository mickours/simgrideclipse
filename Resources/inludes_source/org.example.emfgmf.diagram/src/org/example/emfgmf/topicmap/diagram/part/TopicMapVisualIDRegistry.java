package org.example.emfgmf.topicmap.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.TopicmapPackage;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicMapEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class TopicMapVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = TopicMapDiagramEditorPlugin
			.getInstance().getBundle().getSymbolicName()
			+ "/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (TopicMapEditPart.MODEL_ID.equals(view.getType())) {
				return TopicMapEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry
				.getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(
					Platform.getDebugOption(DEBUG_KEY))) {
				TopicMapDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: "
								+ type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return String.valueOf(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (TopicmapPackage.eINSTANCE.getTopicMap().isSuperTypeOf(
				domainElement.eClass())
				&& isDiagram((TopicMap) domainElement)) {
			return TopicMapEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null
				|| !TopicMapEditPart.MODEL_ID
						.equals(org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry
								.getModelID(containerView))) {
			return -1;
		}
		switch (org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry
				.getVisualID(containerView)) {
		case TopicMapEditPart.VISUAL_ID:
			if (TopicmapPackage.eINSTANCE.getTopic().isSuperTypeOf(
					domainElement.eClass())) {
				return TopicEditPart.VISUAL_ID;
			}
			break;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static boolean canCreateNode(View containerView, int nodeVisualID) {
		String containerModelID = org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry
				.getModelID(containerView);
		if (!TopicMapEditPart.MODEL_ID.equals(containerModelID)) {
			return false;
		}
		int containerVisualID;
		if (TopicMapEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = TopicMapEditPart.VISUAL_ID;
			} else {
				return false;
			}
		}
		switch (containerVisualID) {
		case TopicEditPart.VISUAL_ID:
			if (TopicLabelEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case TopicMapEditPart.VISUAL_ID:
			if (TopicEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case AssociationEditPart.VISUAL_ID:
			if (AssociationLabelEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (TopicmapPackage.eINSTANCE.getAssociation().isSuperTypeOf(
				domainElement.eClass())) {
			return AssociationEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static boolean isDiagram(TopicMap element) {
		return true;
	}

}
