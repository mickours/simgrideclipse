package org.example.emfgmf.topicmap.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.example.emfgmf.topicmap.TopicmapPackage;
import org.example.emfgmf.topicmap.diagram.edit.parts.AssociationLabelEditPart;
import org.example.emfgmf.topicmap.diagram.edit.parts.TopicLabelEditPart;
import org.example.emfgmf.topicmap.diagram.parsers.MessageFormatParser;
import org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry;

/**
 * @generated
 */
public class TopicMapParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser topicLabel_4001Parser;

	/**
	 * @generated
	 */
	private IParser getTopicLabel_4001Parser() {
		if (topicLabel_4001Parser == null) {
			topicLabel_4001Parser = createTopicLabel_4001Parser();
		}
		return topicLabel_4001Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createTopicLabel_4001Parser() {
		EAttribute[] features = new EAttribute[] { TopicmapPackage.eINSTANCE
				.getTopic_Label(), };
		MessageFormatParser parser = new MessageFormatParser(features);
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationLabel_4002Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationLabel_4002Parser() {
		if (associationLabel_4002Parser == null) {
			associationLabel_4002Parser = createAssociationLabel_4002Parser();
		}
		return associationLabel_4002Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationLabel_4002Parser() {
		EAttribute[] features = new EAttribute[] { TopicmapPackage.eINSTANCE
				.getAssociation_Label(), };
		MessageFormatParser parser = new MessageFormatParser(features);
		return parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case TopicLabelEditPart.VISUAL_ID:
			return getTopicLabel_4001Parser();
		case AssociationLabelEditPart.VISUAL_ID:
			return getAssociationLabel_4002Parser();
		}
		return null;
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(TopicMapVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(TopicMapVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (TopicMapElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static class HintAdapter extends ParserHintAdapter {

		/**
		 * @generated
		 */
		private final IElementType elementType;

		/**
		 * @generated
		 */
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		 * @generated
		 */
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
