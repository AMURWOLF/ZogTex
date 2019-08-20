package com.zog.tex.bib.contracts.model.services;

import java.io.InputStream;

import com.zog.core.osgi.nanoservices.Nanoservice;
import com.zog.tex.bib.contracts.model.entities.BibModel;

/**
 * Nanoservice for parsing some InputStream into bibliographic registry model.
 */
public interface BibModelService extends Nanoservice {

	BibModel parse(InputStream input);
	
	BibModel parse(String input);
}
