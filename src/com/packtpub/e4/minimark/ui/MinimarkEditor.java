package com.packtpub.e4.minimark.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class MinimarkEditor extends AbstractTextEditor {

	public MinimarkEditor() {
		setDocumentProvider(new TextFileDocumentProvider());
	}

}
