package org.example.emfgmf.topicmap.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;
import org.example.emfgmf.topicmap.diagram.part.TopicMapVisualIDRegistry;

/**
 * @generated
 */
public class TopicMapNavigatorSorter extends ViewerSorter {

	/**
	 * @generated
	 */
	private static final int GROUP_CATEGORY = 3003;

	/**
	 * @generated
	 */
	public int category(Object element) {
		if (element instanceof TopicMapNavigatorItem) {
			TopicMapNavigatorItem item = (TopicMapNavigatorItem) element;
			return TopicMapVisualIDRegistry.getVisualID(item.getView());
		}
		return GROUP_CATEGORY;
	}

}
