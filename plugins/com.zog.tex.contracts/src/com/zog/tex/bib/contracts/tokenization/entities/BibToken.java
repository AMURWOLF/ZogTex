package com.zog.tex.bib.contracts.tokenization.entities;

/**
 * Some word or sentence in bibliographic input stream.
 */
public class BibToken {
	
	int from;
	int length;
	BibTokenType type;
	String content;
	int row;
	int col;

	public BibToken(int from, BibTokenType type, String content, int row, int col) {

		this.from = from;
		this.length = content.length();
		this.type = type;
		this.content = content;
		this.row = row;
		this.col = col;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public BibTokenType getType() {
		return type;
	}

	public void setType(BibTokenType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
}
