package com.zog.tex.contracts.bib.model.entities;

/**
 * Some properties of a bibliography entry. 
 * There are many other available properties.
 */
public enum BibProp {
	
	TITLE("title"), YEAR("year"), AUTHOR("author");
	
	public final String value;
	
	private BibProp(String value) {
		this.value = value;
	}
}
