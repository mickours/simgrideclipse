package article.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import article.editor.contentassist.TagContentAssistProcessor;
import article.editor.format.DefaultFormattingStrategy;
import article.editor.format.DocTypeFormattingStrategy;
import article.editor.format.PIFormattingStrategy;
import article.editor.format.TextFormattingStrategy;
import article.editor.format.XMLFormattingStrategy;
import article.editor.scanners.CDataScanner;
import article.editor.scanners.XMLPartitionScanner;
import article.editor.scanners.XMLScanner;
import article.editor.scanners.XMLTagScanner;
import article.editor.scanners.XMLTextScanner;

public class XMLConfiguration extends SourceViewerConfiguration
{

    private XMLDoubleClickStrategy doubleClickStrategy;

    private XMLTagScanner tagScanner;

    private XMLScanner scanner;

    private XMLTextScanner textScanner;

    private CDataScanner cdataScanner;

    private ColorManager colorManager;

    public XMLConfiguration(ColorManager colorManager)
    {
        this.colorManager = colorManager;
    }

public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
{
    return new String[]
    {
            IDocument.DEFAULT_CONTENT_TYPE,
            XMLPartitionScanner.XML_COMMENT,
            XMLPartitionScanner.XML_PI,
            XMLPartitionScanner.XML_DOCTYPE,
            XMLPartitionScanner.XML_START_TAG,
            XMLPartitionScanner.XML_END_TAG,
            XMLPartitionScanner.XML_TEXT
    };
}

    public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType)
    {
        if (doubleClickStrategy == null)
            doubleClickStrategy = new XMLDoubleClickStrategy();
        return doubleClickStrategy;
    }

    protected XMLScanner getXMLScanner()
    {
        if (scanner == null)
        {
            scanner = new XMLScanner(colorManager);
            scanner.setDefaultReturnToken(new Token(
                    new TextAttribute(colorManager.getColor(IXMLColorConstants.DEFAULT))));
        }
        return scanner;
    }

    protected XMLTextScanner getXMLTextScanner()
    {
        if (textScanner == null)
        {
            textScanner = new XMLTextScanner(colorManager);
            textScanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager
                    .getColor(IXMLColorConstants.DEFAULT))));
        }
        return textScanner;
    }

    protected CDataScanner getCDataScanner()
    {
        if (cdataScanner == null)
        {
            cdataScanner = new CDataScanner(colorManager);
            cdataScanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager
                    .getColor(IXMLColorConstants.CDATA_TEXT))));
        }
        return cdataScanner;
    }

    protected XMLTagScanner getXMLTagScanner()
    {
        if (tagScanner == null)
        {
            tagScanner = new XMLTagScanner(colorManager);
            tagScanner
                    .setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.TAG))));
        }
        return tagScanner;
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
    {
        PresentationReconciler reconciler = new PresentationReconciler();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getXMLTagScanner());
        reconciler.setDamager(dr, XMLPartitionScanner.XML_START_TAG);
        reconciler.setRepairer(dr, XMLPartitionScanner.XML_START_TAG);

        dr = new DefaultDamagerRepairer(getXMLTagScanner());
        reconciler.setDamager(dr, XMLPartitionScanner.XML_END_TAG);
        reconciler.setRepairer(dr, XMLPartitionScanner.XML_END_TAG);

        dr = new DefaultDamagerRepairer(getXMLScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        dr = new DefaultDamagerRepairer(getXMLScanner());
        reconciler.setDamager(dr, XMLPartitionScanner.XML_DOCTYPE);
        reconciler.setRepairer(dr, XMLPartitionScanner.XML_DOCTYPE);

        dr = new DefaultDamagerRepairer(getXMLScanner());
        reconciler.setDamager(dr, XMLPartitionScanner.XML_PI);
        reconciler.setRepairer(dr, XMLPartitionScanner.XML_PI);

        dr = new DefaultDamagerRepairer(getXMLTextScanner());
        reconciler.setDamager(dr, XMLPartitionScanner.XML_TEXT);
        reconciler.setRepairer(dr, XMLPartitionScanner.XML_TEXT);

        dr = new DefaultDamagerRepairer(getCDataScanner());
        reconciler.setDamager(dr, XMLPartitionScanner.XML_CDATA);
        reconciler.setRepairer(dr, XMLPartitionScanner.XML_CDATA);

        TextAttribute textAttribute = new TextAttribute(colorManager.getColor(IXMLColorConstants.XML_COMMENT));
        NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(textAttribute);
        reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
        reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);

        return reconciler;
    }

    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
    {

        ContentAssistant assistant = new ContentAssistant();

        assistant.setContentAssistProcessor(new TagContentAssistProcessor(getXMLTagScanner()),
                XMLPartitionScanner.XML_START_TAG);
        assistant.enableAutoActivation(true);
        assistant.setAutoActivationDelay(500);
        assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
        return assistant;

    }

    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer)
    {
        ContentFormatter formatter = new ContentFormatter();
        XMLFormattingStrategy formattingStrategy = new XMLFormattingStrategy();
        DefaultFormattingStrategy defaultStrategy = new DefaultFormattingStrategy();
        TextFormattingStrategy textStrategy = new TextFormattingStrategy();
        DocTypeFormattingStrategy doctypeStrategy = new DocTypeFormattingStrategy();
        PIFormattingStrategy piStrategy = new PIFormattingStrategy();
        formatter.setFormattingStrategy(defaultStrategy, IDocument.DEFAULT_CONTENT_TYPE);
        formatter.setFormattingStrategy(textStrategy, XMLPartitionScanner.XML_TEXT);
        formatter.setFormattingStrategy(doctypeStrategy, XMLPartitionScanner.XML_DOCTYPE);
        formatter.setFormattingStrategy(piStrategy, XMLPartitionScanner.XML_PI);
        formatter.setFormattingStrategy(textStrategy, XMLPartitionScanner.XML_CDATA);
        formatter.setFormattingStrategy(formattingStrategy, XMLPartitionScanner.XML_START_TAG);
        formatter.setFormattingStrategy(formattingStrategy, XMLPartitionScanner.XML_END_TAG);

        return formatter;
    }
}