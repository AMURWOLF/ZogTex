package com.zog.tex.contracts.bib.tokenization.services;

import java.io.InputStream;

import com.zog.tex.contracts.bib.tokenization.enities.BibToken;
import com.zog.tex.contracts.util.nanoservice.Nanoservice;

/**
 * Nanoservice for tokenization of bibliographic input stream.
 */
public interface BibTokenService extends Nanoservice {

	Iterable<BibToken> tokenize(InputStream input);
	
	Iterable<BibToken> tokenize(String input);
}
