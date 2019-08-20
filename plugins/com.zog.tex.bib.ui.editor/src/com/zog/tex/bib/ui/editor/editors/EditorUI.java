package com.zog.tex.bib.ui.editor.editors;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.function.Consumer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;

import com.zog.tex.bib.contracts.model.entities.BibEntry;
import com.zog.tex.bib.contracts.model.entities.BibModel;
import com.zog.tex.bib.contracts.model.entities.BibProp;
import com.zog.tex.bib.contracts.model.services.BibModelService;
import com.zog.tex.bib.contracts.tokenization.entities.BibToken;
import com.zog.tex.bib.contracts.tokenization.services.BibTokenService;
import com.zog.tex.bib.ui.editor.Activator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Menu;

public class EditorUI implements PropertyChangeListener {

	// Nanoservices
	BibModelService bibParser = Activator.getNanoservice(BibModelService.class);
	BibTokenService bibTokenizer = Activator.getNanoservice(BibTokenService.class);
	
	BibModel model;
	private boolean dirty;
	private PropertyChangeSupport support;
	ColumnViewerComparator tableComparator;
	private MenuManager menuManager;
	
	TableViewer table;
	StyledText editText;
	Color brown, darkBlue, magenta, darkGreen, warnBackground;
	private ResourcePool pool = new ResourcePool();
	
	
	// Listeners

	private ModifyListener textModifyListener = event -> {
		if (selected() != null) {
			// Try parse the content, block the change if syntax incorrect
			String text = editText.getText();
			try {
				BibModel tempmodel = bibParser.parse(text);
				if (1 != tempmodel.getEntries().size())
					throw new IllegalArgumentException();
				else {
					// Update table info
					BibEntry newEntry = tempmodel.getEntries().get(0);
					// Reset selection
					EditorUI.this.model.replaceEntry(selected(), newEntry);
					table.setSelection(new StructuredSelection(newEntry));
				}
				// Add color to the text
				updateTextContent(editText.getText());
				EditorUI.this.setDirty(true);
				editText.setBackground(null);
			} catch (Exception e) {
				e.printStackTrace();
				editText.setBackground(warnBackground);
			}
		}
	};

	public BibEntry selected() {
		return (BibEntry) ((StructuredSelection) table.getSelection()).getFirstElement();
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public TableViewer getTable() {
		return table;
	}

	public EditorUI() {
		super();
		support = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof BibModel) {

			if ("addEntry".equals(evt.getPropertyName())) {
				// Disable comparator
				table.setComparator(null);
				table.refresh();
				table.setSelection(new StructuredSelection(evt.getNewValue()));
				updateTextContent(evt.getNewValue().toString());
			}
			if ("removeEntry".equals(evt.getPropertyName())) {
				table.refresh();
			}
			if ("entries".equals(evt.getPropertyName())) {
				table.refresh();
			}
			setDirty(true);
		}
	}
	
	public void createUI(Composite parent) {
		brown = new Color(Display.getCurrent(), 147, 2, 22);
		darkBlue = new Color(Display.getCurrent(), 6, 45, 107);
		magenta = new Color(Display.getCurrent(), 66, 6, 14);
		darkGreen = new Color(Display.getCurrent(), 6, 107, 45);
		warnBackground = new Color(Display.getCurrent(), 255, 188, 196);
		pool.add(brown, darkBlue, magenta, darkGreen, warnBackground);

		SashForm form = new SashForm(parent, SWT.VERTICAL);
		form.setLayout(new FillLayout());
		createTable(form);
		createEditPanel(form);
		form.setWeights(new int[] { 70, 30 });
		menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(table.getTable());
		// set the menu on the SWT widget
		table.getTable().setMenu(menu);
	}

	public void dispose() {
		brown.dispose();
		darkBlue.dispose();
		magenta.dispose();
		darkGreen.dispose();
		pool.dispose();
		editText.dispose();
		table = null;
		editText = null;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		boolean oldDirty = this.dirty;
		this.dirty = dirty;
		support.firePropertyChange("dirty", oldDirty, dirty);
	}

	public void save(File target) {
		try (PrintWriter output = new PrintWriter(new FileOutputStream(target))) {
			for (BibEntry entry : model.getEntries()) {
				output.println(entry.toString());
			}
			output.close();
			setDirty(false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createTable(Composite form) {
		Composite tablePanel = new Composite(form, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tablePanel.setLayout(tableColumnLayout);

		table = new TableViewer(tablePanel, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setContentProvider(new ArrayContentProvider());

		insertBibColumn("cite_key", 0, tableColumnLayout, new ColumnWeightData(5, 150, true));
		insertBibColumn("Type", 1, tableColumnLayout, new ColumnWeightData(5, 150, true));
		insertBibColumn("Title", 2, tableColumnLayout, new ColumnWeightData(50, 400, true));
		insertBibColumn("Year", 3, tableColumnLayout, new ColumnPixelData(50));
		insertBibColumn("Author", 4, tableColumnLayout, new ColumnWeightData(40, 400, true));
		
		table.setInput(model.getEntries());
		table.getTable().setLinesVisible(true);
		table.getTable().setHeaderVisible(true);

		table.getTable().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Object selection = ((StructuredSelection) table.getSelection()).getFirstElement();
				if (null != selection) {
					updateTextContent(selection.toString());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				widgetSelected(event);
			}
		});
		
		// Supporting Table Sorting, only when clicking
		tableComparator = new ColumnViewerComparator();
		table.setComparator(null);
	}
	
	public TableViewerColumn insertBibColumn(
			String caption, int index, TableColumnLayout layout, ColumnLayoutData layoutData
	) {
		TableColumn column = new TableColumn(table.getTable(), SWT.NONE);
		column.setText(caption);
		TableViewerColumn columnViewer = new TableViewerColumn(table, column);
		columnViewer.setLabelProvider(new BibEntryColumnLabelProvider(index));
		column.addSelectionListener(new ColumnSelectionListener(index));
		layout.setColumnData(column, layoutData);
		return columnViewer;
	}

	private void createEditPanel(Composite parent) {
		Composite editPanel = new Composite(parent, SWT.NONE);
		editPanel.setLayout(new FillLayout());
		editText = new StyledText(editPanel, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		editText.addModifyListener(textModifyListener);
	}

	void updateTextContent(String content) {
		editText.removeModifyListener(textModifyListener);
		try {
			if (!content.equals(editText.getText())) {
				editText.setText(content);
			}
			
			Iterable<BibToken> bibTokens = bibTokenizer.tokenize(content);
			bibTokens.forEach(new Consumer<BibToken>() {
				@Override
				public void accept(BibToken t) {
					switch (t.getType()) {
					case CITE_KEY:
						editText.setStyleRange(new StyleRange(t.getFrom(), t.getLength(), brown, null, SWT.BOLD));
						break;
					case ENTRY_TYPE:
						editText.setStyleRange(new StyleRange(t.getFrom(), t.getLength(), darkBlue, null, SWT.BOLD));
						break;
					case PROP_KEY:
						editText.setStyleRange(new StyleRange(t.getFrom(), t.getLength(), magenta, null, SWT.BOLD));
						break;
					case PROP_VAL:
						editText.setStyleRange(new StyleRange(t.getFrom(), t.getLength(), darkGreen, null, SWT.NORMAL));
						break;
					default:
						break;
					}
				}
			});
		} finally {
			editText.addModifyListener(textModifyListener);
		}
	}

	public BibModel getModel() {
		return model;
	}

	public void setModel(BibModel model) {
		if (this.model != null)
			this.model.removePropertyChangeListener(this);
		this.model = model;
		if (this.model != null)
			this.model.addPropertyChangeListener(this);
	}

	private static class BibEntryColumnLabelProvider extends ColumnLabelProvider {

		private int index;

		public BibEntryColumnLabelProvider(int index) {
			super();
			this.index = index;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof BibEntry) {
				return extract(element, index);
			}
			return super.getText(element);
		}

		static String extract(Object element, int index) {
			BibEntry entry = (BibEntry) element;
			switch (index) {
			case 0:
				return entry.getCiteKey();
			case 1:
				return entry.getType();
			case 2:
				return entry.getProperty(BibProp.TITLE.value);
			case 3:
				return entry.getProperty(BibProp.YEAR.value);
			case 4:
				return entry.getProperty(BibProp.AUTHOR.value);
			default:
				return "";
			}
		}
	}
		
	protected class ColumnViewerComparator extends ViewerComparator {
		private int propertyIndex;
		private static final int DESCENDING = 1;
		private int direction = DESCENDING;

		public ColumnViewerComparator() {
			this.propertyIndex = 0;
			direction = DESCENDING;
		}

		public int getDirection() {
			return direction == 1 ? SWT.DOWN : SWT.UP;
		}

		public void setColumn(int column) {
			if (column == this.propertyIndex) {
				// Same column as last sort; toggle the direction
				direction = 1 - direction;
			} else {
				// New column; do an ascending sort
				this.propertyIndex = column;
				direction = DESCENDING;
			}
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			String data1 = BibEntryColumnLabelProvider.extract(e1, propertyIndex);
			String data2 = BibEntryColumnLabelProvider.extract(e2, propertyIndex);
			if (null == data1)
				data1 = "";
			if (null == data2)
				data2 = "";
			int rc = data1.compareTo(data2);
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
			return rc;
		}
	}

	private class ColumnSelectionListener implements SelectionListener {

		int index = -1;

		public ColumnSelectionListener(int index) {
			super();
			this.index = index;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			tableComparator.setColumn(this.index);
			table.setComparator(tableComparator);
			table.refresh();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}
	}
}
