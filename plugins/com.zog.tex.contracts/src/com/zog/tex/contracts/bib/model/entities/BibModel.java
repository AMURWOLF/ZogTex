package com.zog.tex.contracts.bib.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a bibliographic information file.
 */
public class BibModel {

	private List<BibEntry> entries;

	public BibModel() {
		entries = new ArrayList<>();
	}

	public List<BibEntry> getEntries() {
		return entries;
	}

}
