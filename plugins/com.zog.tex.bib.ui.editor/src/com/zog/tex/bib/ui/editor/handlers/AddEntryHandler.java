package com.zog.tex.bib.ui.editor.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.zog.tex.bib.contracts.model.entities.BibEntry;
import com.zog.tex.bib.ui.editor.editors.BibEditor;

public class AddEntryHandler extends BibHandler {

	@Override
	public Object executeWithEditor(ExecutionEvent event, BibEditor editor) throws ExecutionException {
		BibEntry newEntry = new BibEntry();
		newEntry.setCiteKey("new_entry");
		editor.getUi().getModel().addEntry(newEntry);
		return null;
	}
}
