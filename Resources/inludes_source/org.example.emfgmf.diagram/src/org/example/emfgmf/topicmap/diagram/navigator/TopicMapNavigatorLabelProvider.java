package org.example.emfgmf.topicmap.diagram.navigator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.example.emfgmf.topicmap.TopicMap;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicMapEditPart;
import org.example.emfgmf.topicmap.diagram.part.TopicMapDiagramEditorPlugin;
import org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry;
import org.example.emfgmf.topicmap.diagram.providers.TopicMapElementTypes;
import org.example.emfgmf.topicmap.diagram.providers.TopicMapParserProvider;

/**
 * @generated
 */
public class TopicMapNavigatorLabelProvider extends LabelProvider implements
		ICommonLabelProvider, ITreePathLabelProvider {

	/**
	 * @generated
	 */
	static {
		TopicMapDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put(
						"Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
		TopicMapDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put(
						"Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public void updateLabel(ViewerLabel label, TreePath elementPath) {
		Object element = elementPath.getLastSegment();
		if (element instanceof TopicMapNavigatorItem
				&& !isOwnView(((TopicMapNavigatorItem) element).getView())) {
			return;
		}
		label.setText(getText(element));
		label.setImage(getImage(element));
	}

	/**
	 * @generated
	 */
	public Image getImage(Object element) {
		if (element instanceof TopicMapNavigatorGroup) {
			TopicMapNavigatorGroup group = (TopicMapNavigatorGroup) element;
			return TopicMapDiagramEditorPlugin.getInstance().getBundledImage(
					group.getIcon());
		}

		if (element instanceof TopicMapNavigatorItem) {
			TopicMapNavigatorItem navigatorItem = (TopicMapNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return super.getImage(element);
			}
			return getImage(navigatorItem.getView());
		}

		return super.getImage(element);
	}

	/**
	 * @generated
	 */
	public Image getImage(View view) {
		switch (TopicMapVisualIDRegistry.getVisualID(view)) {
		case TopicMapEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Diagram?http://www.example.org/emfgmf?TopicMap", TopicMapElementTypes.TopicMap_79); //$NON-NLS-1$
		case TopicEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.example.org/emfgmf?Topic", TopicMapElementTypes.Topic_1001); //$NON-NLS-1$
		case AssociationEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Link?http://www.example.org/emfgmf?Association", TopicMapElementTypes.Association_3001); //$NON-NLS-1$
		}
		return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private Image getImage(String key, IElementType elementType) {
		ImageRegistry imageRegistry = TopicMapDiagramEditorPlugin.getInstance()
				.getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null && elementType != null
				&& TopicMapElementTypes.isKnownElementType(elementType)) {
			image = TopicMapElementTypes.getImage(elementType);
			imageRegistry.put(key, image);
		}

		if (image == null) {
			image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
			imageRegistry.put(key, image);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public String getText(Object element) {
		if (element instanceof TopicMapNavigatorGroup) {
			TopicMapNavigatorGroup group = (TopicMapNavigatorGroup) element;
			return group.getGroupName();
		}

		if (element instanceof TopicMapNavigatorItem) {
			TopicMapNavigatorItem navigatorItem = (TopicMapNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return null;
			}
			return getText(navigatorItem.getView());
		}

		return super.getText(element);
	}

	/**
	 * @generated
	 */
	public String getText(View view) {
		if (view.getElement() != null && view.getElement().eIsProxy()) {
			return getUnresolvedDomainElementProxyText(view);
		}
		switch (TopicMapVisualIDRegistry.getVisualID(view)) {
		case TopicMapEditPart.VISUAL_ID:
			return getTopicMap_79Text(view);
		case TopicEditPart.VISUAL_ID:
			return getTopic_1001Text(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_3001Text(view);
		}
		return getUnknownElementText(view);
	}

	/**
	 * @generated
	 */
	private String getTopicMap_79Text(View view) {
		TopicMap domainModelElement = (TopicMap) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getLabel();
		} else {
			TopicMapDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 79); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getTopic_1001Text(View view) {
		IAdaptable hintAdapter = new TopicMapParserProvider.HintAdapter(
				TopicMapElementTypes.Topic_1001,
				(view.getElement() != null ? view.getElement() : view),
				TopicMapVisualIDRegistry.getType(TopicLabelEditPart.VISUAL_ID));
		IParser parser = ParserService.getInstance().getParser(hintAdapter);

		if (parser != null) {
			return parser.getPrintString(hintAdapter, ParserOptions.NONE
					.intValue());
		} else {
			TopicMapDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 4001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}

	}

	/**
	 * @generated
	 */
	private String getAssociation_3001Text(View view) {
		IAdaptable hintAdapter = new TopicMapParserProvider.HintAdapter(
				TopicMapElementTypes.Association_3001,
				(view.getElement() != null ? view.getElement() : view),
				TopicMapVisualIDRegistry
						.getType(AssociationLabelEditPart.VISUAL_ID));
		IParser parser = ParserService.getInstance().getParser(hintAdapter);

		if (parser != null) {
			return parser.getPrintString(hintAdapter, ParserOptions.NONE
					.intValue());
		} else {
			TopicMapDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 4002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}

	}

	/**
	 * @generated
	 */
	private String getUnknownElementText(View view) {
		return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	private String getUnresolvedDomainElementProxyText(View view) {
		return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	 * @generated
	 */
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public String getDescription(Object anElement) {
		return null;
	}

	/**
	 * @generated
	 */
	private boolean isOwnView(View view) {
		return TopicMapEditPart.MODEL_ID.equals(TopicMapVisualIDRegistry
				.getModelID(view));
	}

}
