package com.zog.tex.bib.ui.editor.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.zog.tex.bib.ui.editor.Activator;
import com.zog.tex.contracts.bib.model.entities.BibModel;
import com.zog.tex.contracts.bib.model.services.BibModelService;
import com.zog.tex.contracts.bib.tokenization.services.BibTokenService;


public class BibEditor extends EditorPart implements PropertyChangeListener {

	// Nanoservices
	BibModelService bibParser = Activator.getNanoservice(BibModelService.class);
	BibTokenService bibTokenizer = Activator.getNanoservice(BibTokenService.class);

	private BibModel model;
	private EditorUI ui;

	public BibEditor() {
		super();
		ui = new EditorUI();
		ui.addPropertyChangeListener(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		setContentDescription(input.getToolTipText());
		setTitleToolTip(input.getToolTipText());

		IPath path = ((IPathEditorInput) input).getPath();
		try (var stream = new FileInputStream(path.toFile())) {
			model = bibParser.parse(stream);
			ui.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		ui.createUI(parent);
		getSite().registerContextMenu(ui.getMenuManager(), ui.getTable());
		getSite().setSelectionProvider(ui.getTable());
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		ui.save(((IPathEditorInput) getEditorInput()).getPath().toFile());
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void dispose() {
		super.dispose();
		ui.dispose();
	}

	@Override
	public boolean isDirty() {
		return ui.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == ui) {
			if ("dirty".equals(evt.getPropertyName())) {
				firePropertyChange(PROP_DIRTY);
			}
		}
	}
	
	public BibModel getModel() {
		return model;
	}

	public EditorUI getUi() {
		return ui;
	}

}
