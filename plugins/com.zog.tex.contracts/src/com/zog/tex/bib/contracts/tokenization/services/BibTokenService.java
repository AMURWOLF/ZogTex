package com.zog.tex.bib.contracts.tokenization.services;

import java.io.InputStream;

import com.zog.core.osgi.nanoservices.Nanoservice;
import com.zog.tex.bib.contracts.tokenization.entities.BibToken;

/**
 * Nanoservice for tokenization of bibliographic input stream.
 */
public interface BibTokenService extends Nanoservice {

	Iterable<BibToken> tokenize(InputStream input);
	
	Iterable<BibToken> tokenize(String input);
}
