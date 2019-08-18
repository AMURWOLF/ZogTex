package com.zog.tex.contracts.bib.model.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A representation of a bibliographic information file.
 */
public class BibModel {

	private List<BibEntry> entries;
	private PropertyChangeSupport support;

	public BibModel() {
		support = new PropertyChangeSupport(this);
		entries = new ArrayList<>();
	}
	
	public void setEntries(List<BibEntry> entries) {
		this.entries = entries;
	}

	public List<BibEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public void addEntry(BibEntry entry) {
		this.entries.add(entry);
		support.firePropertyChange("addEntry", null, entry);
	}

	public void removeEntry(BibEntry entry) {
		this.entries.remove(entry);
		support.firePropertyChange("removeEntry", entry, null);
	}

	public void replaceEntry(BibEntry old, BibEntry newe) {
		int index = this.entries.indexOf(old);
		if (-1 != index) {
			this.entries.set(index, newe);
			support.fireIndexedPropertyChange("entries", index, old, newe);
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

}
