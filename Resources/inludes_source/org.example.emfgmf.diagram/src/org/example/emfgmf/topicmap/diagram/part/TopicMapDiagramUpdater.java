package org.example.emfgmf.topicmap.diagram.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.example.emfgmf.topicmap.Association;
import org.example.emfgmf.topicmap.Topic;
import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.TopicmapPackage;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicMapEditPart;
import org.example.emfgmf.topicmap.diagram.providers.TopicMapElementTypes;

/**
 * @generated
 */
public class TopicMapDiagramUpdater {

	/**
	 * @generated
	 */
	public static List getSemanticChildren(View view) {
		switch (TopicMapVisualIDRegistry.getVisualID(view)) {
		case TopicMapEditPart.VISUAL_ID:
			return getTopicMap_79SemanticChildren(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getTopicMap_79SemanticChildren(View view) {
		if (!view.isSetElement()) {
			return Collections.EMPTY_LIST;
		}
		TopicMap modelElement = (TopicMap) view.getElement();
		List result = new LinkedList();
		for (Iterator it = modelElement.getTopics().iterator(); it.hasNext();) {
			Topic childElement = (Topic) it.next();
			int visualID = TopicMapVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == TopicEditPart.VISUAL_ID) {
				result.add(new TopicMapNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List getContainedLinks(View view) {
		switch (TopicMapVisualIDRegistry.getVisualID(view)) {
		case TopicMapEditPart.VISUAL_ID:
			return getTopicMap_79ContainedLinks(view);
		case TopicEditPart.VISUAL_ID:
			return getTopic_1001ContainedLinks(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_3001ContainedLinks(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getIncomingLinks(View view) {
		switch (TopicMapVisualIDRegistry.getVisualID(view)) {
		case TopicEditPart.VISUAL_ID:
			return getTopic_1001IncomingLinks(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_3001IncomingLinks(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getOutgoingLinks(View view) {
		switch (TopicMapVisualIDRegistry.getVisualID(view)) {
		case TopicEditPart.VISUAL_ID:
			return getTopic_1001OutgoingLinks(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_3001OutgoingLinks(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getTopicMap_79ContainedLinks(View view) {
		TopicMap modelElement = (TopicMap) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getContainedTypeModelFacetLinks_Association_3001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getTopic_1001ContainedLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getAssociation_3001ContainedLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getTopic_1001IncomingLinks(View view) {
		Topic modelElement = (Topic) view.getElement();
		Map crossReferences = EcoreUtil.CrossReferencer.find(view.eResource()
				.getResourceSet().getResources());
		List result = new LinkedList();
		result.addAll(getIncomingTypeModelFacetLinks_Association_3001(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getAssociation_3001IncomingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getTopic_1001OutgoingLinks(View view) {
		Topic modelElement = (Topic) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getOutgoingTypeModelFacetLinks_Association_3001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getAssociation_3001OutgoingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	private static Collection getContainedTypeModelFacetLinks_Association_3001(
			TopicMap container) {
		Collection result = new LinkedList();
		for (Iterator links = container.getAssociations().iterator(); links
				.hasNext();) {
			Object linkObject = links.next();
			if (false == linkObject instanceof Association) {
				continue;
			}
			Association link = (Association) linkObject;
			if (AssociationEditPart.VISUAL_ID != TopicMapVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			Topic dst = link.getRight();
			Topic src = link.getLeft();
			result.add(new TopicMapLinkDescriptor(src, dst, link,
					TopicMapElementTypes.Association_3001,
					AssociationEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection getIncomingTypeModelFacetLinks_Association_3001(
			Topic target, Map crossReferences) {
		Collection result = new LinkedList();
		Collection settings = (Collection) crossReferences.get(target);
		for (Iterator it = settings.iterator(); it.hasNext();) {
			EStructuralFeature.Setting setting = (EStructuralFeature.Setting) it
					.next();
			if (setting.getEStructuralFeature() != TopicmapPackage.eINSTANCE
					.getAssociation_Right()
					|| false == setting.getEObject() instanceof Association) {
				continue;
			}
			Association link = (Association) setting.getEObject();
			if (AssociationEditPart.VISUAL_ID != TopicMapVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			Topic src = link.getLeft();
			result.add(new TopicMapLinkDescriptor(src, target, link,
					TopicMapElementTypes.Association_3001,
					AssociationEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection getOutgoingTypeModelFacetLinks_Association_3001(
			Topic source) {
		TopicMap container = null;
		// Find container element for the link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null && container == null; element = element
				.eContainer()) {
			if (element instanceof TopicMap) {
				container = (TopicMap) element;
			}
		}
		if (container == null) {
			return Collections.EMPTY_LIST;
		}
		Collection result = new LinkedList();
		for (Iterator links = container.getAssociations().iterator(); links
				.hasNext();) {
			Object linkObject = links.next();
			if (false == linkObject instanceof Association) {
				continue;
			}
			Association link = (Association) linkObject;
			if (AssociationEditPart.VISUAL_ID != TopicMapVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			Topic dst = link.getRight();
			Topic src = link.getLeft();
			if (src != source) {
				continue;
			}
			result.add(new TopicMapLinkDescriptor(src, dst, link,
					TopicMapElementTypes.Association_3001,
					AssociationEditPart.VISUAL_ID));
		}
		return result;
	}

}
