/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.core.internal.contenttype;

public class HeadParserToken {
	private int fStart;

	private String fText;
	private String fType;

	protected HeadParserToken() {
		super();
	}

	public HeadParserToken(String type, int start, String text) {
		this();
		fType = type;
		fStart = start;
		fText = text;

	}

	public String getText() {
		return fText;
	}

	public String getType() {
		return fType;
	}

	public String toString() {
		return ("text: " + fText + " offset: " + fStart + " type: " + fType); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
