package org.example.emfgmf.topicmap.diagram.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.example.emfgmf.topicmap.diagram.providers.TopicMapElementTypes;

/**
 * @generated
 */
public class TopicMapPaletteFactory {

	/**
	 * @generated
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createTopicmap1Group());
	}

	/**
	 * Creates "topicmap" palette tool group
	 * @generated
	 */
	private PaletteContainer createTopicmap1Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.Topicmap1Group_title);
		paletteContainer.add(createTopic1CreationTool());
		paletteContainer.add(createAssociation2CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTopic1CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(TopicMapElementTypes.Topic_1001);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Topic1CreationTool_title,
				Messages.Topic1CreationTool_desc, types);
		entry.setSmallIcon(TopicMapElementTypes
				.getImageDescriptor(TopicMapElementTypes.Topic_1001));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAssociation2CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(TopicMapElementTypes.Association_3001);
		LinkToolEntry entry = new LinkToolEntry(
				Messages.Association2CreationTool_title,
				Messages.Association2CreationTool_desc, types);
		entry.setSmallIcon(TopicMapElementTypes
				.getImageDescriptor(TopicMapElementTypes.Association_3001));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				List elementTypes) {
			super(title, description, null, null);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				List relationshipTypes) {
			super(title, description, null, null);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
