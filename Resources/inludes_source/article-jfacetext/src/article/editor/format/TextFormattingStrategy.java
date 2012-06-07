/*
 * Created on Oct 11, 2004
 */
package article.editor.format;

/**
 * @author Phil Zoio
 */
public class TextFormattingStrategy extends DefaultFormattingStrategy
{

	private static final String lineSeparator = System.getProperty("line.separator");

	public TextFormattingStrategy()
	{
		super();
	}

	public String format(String content, boolean isLineStart, String indentation, int[] positions)
	{
		if (indentation.length() == 0)
			return content;
		return lineSeparator + content.trim() + lineSeparator + indentation;
	}

}