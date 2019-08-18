package com.zog.tex.bib.tokenizing.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.zog.tex.contracts.bib.model.exceptions.BibParseException;
import com.zog.tex.contracts.bib.tokenization.enities.BibToken;
import com.zog.tex.contracts.bib.tokenization.enities.BibTokenType;
import com.zog.tex.contracts.bib.tokenization.services.BibTokenService;

public class BibTokenServiceImpl implements BibTokenService {
	
	private static int BUFFER_SIZE = 8192;
	
	static enum State {
		READ_ENTRY, 
		APPEND_ENTRY, 
		READ_ID, 
		READ_PROPKEY, 
		READ_PROPVAL, 
		APPEND_PROPVAL, 
		AFTER_PROPVAL
	}
	
	@Override
	public Iterable<BibToken> tokenize(InputStream input) {

		List<BibToken> bibTokens = new ArrayList<>();
		State state = State.READ_ENTRY;
		byte[] buffer = new byte[BUFFER_SIZE];
		int readCount = 0;
		int charCounter = 0;
		StringBuilder string = new StringBuilder();
		int stackCount = 0;
		boolean hasBracket = false;
		boolean skipSpace = true;
		int row = 1;
		int col = 0;
		int tokenStart = 0;

		while (true) {
			try {
				readCount = input.read(buffer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			for (int i = 0; i < readCount; i++) {
				int character = buffer[i];

				col++;
				// Ignore CRLF
				if (character == '\n' || character == '\r') {
					charCounter++;
					row++;
					col = 0;
					continue;
				}
				// Space
				if (skipSpace && (character == ' ' || character == '\t')) {
					charCounter++;
					continue;
				}

				// State Transition
				switch (state) {
				case READ_ENTRY:
					if ('@' == character) {
						state = State.APPEND_ENTRY;
						string.setLength(0);
					} else {
						// Ignore invalid character silently
						// throw new BibParseException(rowCount, colCount);
					}
					break;
				case APPEND_ENTRY:
					if ('{' == character) {
						state = State.READ_ID;
						String data = string.toString();
						bibTokens.add(new BibToken(tokenStart, BibTokenType.ENTRY_TYPE, data.trim(), row, col));
						string.setLength(0);
					} else {
						if (string.length() == 0)
							tokenStart = charCounter;
						string.append((char) character);
					}
					break;
				case READ_ID:
					if (',' == character) {
						state = State.READ_PROPKEY;
						String data = string.toString();
						bibTokens.add(new BibToken(charCounter - data.length(), BibTokenType.CITE_KEY, data.trim(), row,
								col - data.length()));
						string.setLength(0);
					} else {
						if (string.length() == 0)
							tokenStart = charCounter;
						string.append((char) character);
					}
					break;
				case READ_PROPKEY:
					if ('=' == character) {
						state = State.READ_PROPVAL;

						String data = string.toString();
						bibTokens.add(new BibToken(tokenStart, BibTokenType.PROP_KEY, data.trim(), row, col));

						string.setLength(0);
					} else if ('}' == character) {
						state = State.READ_ENTRY;
					} else {
						if (string.length() == 0)
							tokenStart = charCounter;
						string.append((char) character);
					}
					break;
				case READ_PROPVAL:
					if ('{' == character) {
						string.setLength(0);
						hasBracket = true;
						state = State.APPEND_PROPVAL;
						skipSpace = false;
					} else if (',' == character) {
						state = State.READ_PROPKEY;
						bibTokens.add(new BibToken(charCounter, BibTokenType.PROP_VAL, "", row, col));
					} else {
						if (string.length() == 0)
							tokenStart = charCounter;
						string.append((char) character);
						hasBracket = false;
						state = State.APPEND_PROPVAL;
						skipSpace = false;
					}
					break;
				case APPEND_PROPVAL:
					if ('{' == character) {
						stackCount++;
						if (string.length() == 0)
							tokenStart = charCounter;
						string.append((char) character);
					} else if ('}' == character) {
						if (stackCount == 0) {
							state = State.AFTER_PROPVAL;
							skipSpace = true;

							String data = string.toString();
							bibTokens.add(new BibToken(tokenStart, BibTokenType.PROP_VAL, data.trim(), row, col));

							string.setLength(0);
						} else {
							stackCount--;
							string.append((char) character);
						}
					} else if (',' == character && hasBracket == false) {
						state = State.READ_PROPKEY;
						skipSpace = true;

						String data = string.toString();
						bibTokens.add(new BibToken(tokenStart, BibTokenType.PROP_VAL, data.trim(), row, col));

						string.setLength(0);
					} else {
						if (string.length() == 0)
							tokenStart = charCounter;
						string.append((char) character);
					}
					break;
				case AFTER_PROPVAL:
					if (',' == character) {
						state = State.READ_PROPKEY;
					} else if('}' == character) {
						state = State.READ_ENTRY;
					} else {
						throw new BibParseException(charCounter);
					}
					break;
				default:
					break;
				}
				charCounter++;
			}

			if (readCount != BUFFER_SIZE)
				break;
		}

		return bibTokens;
	}

	@Override
	public Iterable<BibToken> tokenize(String input) {
		InputStream textStream = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));
		return tokenize(textStream);
	}

}
