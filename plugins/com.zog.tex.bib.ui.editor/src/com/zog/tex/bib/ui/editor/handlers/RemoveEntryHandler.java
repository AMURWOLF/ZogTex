package com.zog.tex.bib.ui.editor.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.zog.tex.bib.ui.editor.editors.BibEditor;
import com.zog.tex.contracts.bib.model.entities.BibEntry;

public class RemoveEntryHandler extends BibHandler {

	@Override
	public Object executeWithEditor(ExecutionEvent event, BibEditor editor) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		BibEntry entry = editor.getUi().selected();
		if (null != entry) {
			editor.getUi().getModel().removeEntry(entry);
		} else {
			MessageDialog.openWarning(window.getShell(), "No entry selected", "Please select an entry to delete");

		}
		return null;
	}
}
