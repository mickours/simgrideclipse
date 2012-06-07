package article.editor;

import java.util.ResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.xml.sax.helpers.LocatorImpl;

import article.EditorArticlePlugin;
import article.editor.markers.MarkingErrorHandler;
import article.editor.outline.EditorContentOutlinePage;
import article.editor.xml.XMLParser;


public class XMLEditor extends TextEditor
{

	private ColorManager colorManager;
	private IEditorInput input;
	private EditorContentOutlinePage outlinePage;

	public XMLEditor()
	{
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}

	public void dispose()
	{
		colorManager.dispose();
		if (outlinePage != null)
			outlinePage.setInput(null);
		super.dispose();
	}

	protected void doSetInput(IEditorInput newInput) throws CoreException
	{
		super.doSetInput(newInput);
		this.input = newInput;

		if (outlinePage != null)
			outlinePage.setInput(input);
		
		validateAndMark();
	}

	protected void editorSaved()
	{
		super.editorSaved();


		if (outlinePage != null)
			outlinePage.update();	
	
		//we validate and mark document here
		validateAndMark();

	}

	protected void validateAndMark()
	{
		try
		{
			IDocument document = getInputDocument();
			String text = document.get();
			MarkingErrorHandler markingErrorHandler = new MarkingErrorHandler(getInputFile(), document);
			markingErrorHandler.setDocumentLocator(new LocatorImpl());
			markingErrorHandler.removeExistingMarkers();
			
			XMLParser parser = new XMLParser();
			parser.setErrorHandler(markingErrorHandler);
			parser.doParse(text);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
	}

	protected IDocument getInputDocument()
	{
		IDocument document = getDocumentProvider().getDocument(input);
		return document;
	}

	protected IFile getInputFile()
	{
		IFileEditorInput ife = (IFileEditorInput) input;
		IFile file = ife.getFile();
		return file;
	}
	
	
	public IEditorInput getInput()
	{
		return input;
	}
	
	
	/**
	 * Needed for content assistant
	 */
	protected void createActions()
	{
		super.createActions();
		ResourceBundle bundle = EditorArticlePlugin.getDefault().getResourceBundle();
		setAction("ContentFormatProposal", new TextOperationAction(bundle, "ContentFormatProposal.", this,
				ISourceViewer.FORMAT));
		setAction("ContentAssistProposal", new TextOperationAction(bundle, "ContentAssistProposal.", this,
				ISourceViewer.CONTENTASSIST_PROPOSALS));
		setAction("ContentAssistTip", new TextOperationAction(bundle, "ContentAssistTip.", this,
				ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));

	}
	
	
	public Object getAdapter(Class required)
	{

		if (IContentOutlinePage.class.equals(required))
		{
			if (outlinePage == null)
			{
				outlinePage = new EditorContentOutlinePage(this);
				if (getEditorInput() != null)
					outlinePage.setInput(getEditorInput());
			}
			return outlinePage;
		}

		return super.getAdapter(required);
		
	}
	

}