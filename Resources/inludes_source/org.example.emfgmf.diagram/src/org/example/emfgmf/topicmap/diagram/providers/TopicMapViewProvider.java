package org.example.emfgmf.topicmap.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.View;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicMapEditPart;
import org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry;
import org.example.emfgmf.topicmap.diagram.view.factories.AssociationLabelViewFactory;
import org.example.emfgmf.topicmap.diagram.view.factories.AssociationViewFactory;
import org.example.emfgmf.topicmap.diagram.view.factories.TopicLabelViewFactory;
import org.example.emfgmf.topicmap.diagram.view.factories.TopicMapViewFactory;
import org.example.emfgmf.topicmap.diagram.view.factories.TopicViewFactory;

/**
 * @generated
 */
public class TopicMapViewProvider extends AbstractViewProvider {

	/**
	 * @generated
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		EObject semanticElement = getSemanticElement(semanticAdapter);
		if (TopicMapEditPart.MODEL_ID.equals(diagramKind)
				&& TopicMapVisualIDRegistry.getDiagramVisualID(semanticElement) != -1) {
			return TopicMapViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Class getNodeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		if (containerView == null) {
			return null;
		}
		IElementType elementType = getSemanticElementType(semanticAdapter);
		EObject domainElement = getSemanticElement(semanticAdapter);
		int visualID;
		if (semanticHint == null) {
			// Semantic hint is not specified. Can be a result of call from CanonicalEditPolicy.
			// In this situation there should be NO elementType, visualID will be determined
			// by VisualIDRegistry.getNodeVisualID() for domainElement.
			if (elementType != null || domainElement == null) {
				return null;
			}
			visualID = TopicMapVisualIDRegistry.getNodeVisualID(containerView,
					domainElement);
		} else {
			visualID = TopicMapVisualIDRegistry.getVisualID(semanticHint);
			if (elementType != null) {
				// Semantic hint is specified together with element type.
				// Both parameters should describe exactly the same diagram element.
				// In addition we check that visualID returned by VisualIDRegistry.getNodeVisualID() for
				// domainElement (if specified) is the same as in element type.
				if (!TopicMapElementTypes.isKnownElementType(elementType)
						|| (!(elementType instanceof IHintedType))) {
					return null; // foreign element type
				}
				String elementTypeHint = ((IHintedType) elementType)
						.getSemanticHint();
				if (!semanticHint.equals(elementTypeHint)) {
					return null; // if semantic hint is specified it should be the same as in element type
				}
				if (domainElement != null
						&& visualID != TopicMapVisualIDRegistry
								.getNodeVisualID(containerView, domainElement)) {
					return null; // visual id for node EClass should match visual id from element type
				}
			} else {
				// Element type is not specified. Domain element should be present (except pure design elements).
				// This method is called with EObjectAdapter as parameter from:
				//   - ViewService.createNode(View container, EObject eObject, String type, PreferencesHint preferencesHint) 
				//   - generated ViewFactory.decorateView() for parent element
				if (!TopicMapEditPart.MODEL_ID.equals(TopicMapVisualIDRegistry
						.getModelID(containerView))) {
					return null; // foreign diagram
				}
				switch (visualID) {
				case TopicEditPart.VISUAL_ID:
					if (domainElement == null
							|| visualID != TopicMapVisualIDRegistry
									.getNodeVisualID(containerView,
											domainElement)) {
						return null; // visual id in semantic hint should match visual id for domain element
					}
					break;
				case TopicLabelEditPart.VISUAL_ID:
					if (TopicEditPart.VISUAL_ID != TopicMapVisualIDRegistry
							.getVisualID(containerView)
							|| containerView.getElement() != domainElement) {
						return null; // wrong container
					}
					break;
				case AssociationLabelEditPart.VISUAL_ID:
					if (AssociationEditPart.VISUAL_ID != TopicMapVisualIDRegistry
							.getVisualID(containerView)
							|| containerView.getElement() != domainElement) {
						return null; // wrong container
					}
					break;
				default:
					return null;
				}
			}
		}
		return getNodeViewClass(containerView, visualID);
	}

	/**
	 * @generated
	 */
	protected Class getNodeViewClass(View containerView, int visualID) {
		if (containerView == null
				|| !TopicMapVisualIDRegistry.canCreateNode(containerView,
						visualID)) {
			return null;
		}
		switch (visualID) {
		case TopicEditPart.VISUAL_ID:
			return TopicViewFactory.class;
		case TopicLabelEditPart.VISUAL_ID:
			return TopicLabelViewFactory.class;
		case AssociationLabelEditPart.VISUAL_ID:
			return AssociationLabelViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (!TopicMapElementTypes.isKnownElementType(elementType)
				|| (!(elementType instanceof IHintedType))) {
			return null; // foreign element type
		}
		String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
		if (elementTypeHint == null) {
			return null; // our hint is visual id and must be specified
		}
		if (semanticHint != null && !semanticHint.equals(elementTypeHint)) {
			return null; // if semantic hint is specified it should be the same as in element type
		}
		int visualID = TopicMapVisualIDRegistry.getVisualID(elementTypeHint);
		EObject domainElement = getSemanticElement(semanticAdapter);
		if (domainElement != null
				&& visualID != TopicMapVisualIDRegistry
						.getLinkWithClassVisualID(domainElement)) {
			return null; // visual id for link EClass should match visual id from element type
		}
		return getEdgeViewClass(visualID);
	}

	/**
	 * @generated
	 */
	protected Class getEdgeViewClass(int visualID) {
		switch (visualID) {
		case AssociationEditPart.VISUAL_ID:
			return AssociationViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	private IElementType getSemanticElementType(IAdaptable semanticAdapter) {
		if (semanticAdapter == null) {
			return null;
		}
		return (IElementType) semanticAdapter.getAdapter(IElementType.class);
	}
}
