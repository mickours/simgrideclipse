package simgrideclipseplugin.editors.outline;

import java.util.List;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.w3c.dom.Element;

import simgrideclipseplugin.model.ModelHelper;

public class OutlineContentProvider implements ITreeContentProvider {

	private Element root = null;
	private IEditorInput input;
	private IDocumentProvider documentProvider;

	protected final static String TAG_POSITIONS = "__tag_positions";
	protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(
			TAG_POSITIONS);

	public OutlineContentProvider(IDocumentProvider provider) {
		super();
		this.documentProvider = provider;
	}

	@SuppressWarnings("rawtypes")
	public Object[] getChildren(Object parentElement) {
		if (parentElement == input) {
			if (root == null)
				return new Object[0];
			List childrenElements = ModelHelper.getChildren(root);
			if (childrenElements != null)
				return childrenElements.toArray();
		} else {
			Element parent = (Element) parentElement;
			List childrenElements = ModelHelper.getChildren(parent);
			if (childrenElements != null)
				return childrenElements.toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof Element)
			return ((Element) element).getParentNode();
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element == input)
			return true;
		else {
			return ModelHelper.getChildren((Element) element).size() > 0;
		}
	}

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		if (root == null)
			return new Object[0];
		List childrenDTDElements = ModelHelper.getNoConnectionChildren(root);
		if (childrenDTDElements != null)
			return childrenDTDElements.toArray();
		return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		if (oldInput != null) {
			IDocument document = documentProvider.getDocument(oldInput);
			if (document != null) {
				try {
					document.removePositionCategory(TAG_POSITIONS);
				} catch (BadPositionCategoryException x) {
				}
				document.removePositionUpdater(positionUpdater);
			}
		}

		input = (IEditorInput) newInput;

		if (newInput != null) {
			IDocument document = documentProvider.getDocument(newInput);
			if (document != null) {
				document.addPositionCategory(TAG_POSITIONS);
				document.addPositionUpdater(positionUpdater);
				Element rootElement = null;
				rootElement = ModelHelper.getRootElement(input);
				if (rootElement != null) {
					root = rootElement;
				}
			}
		}
	}
}