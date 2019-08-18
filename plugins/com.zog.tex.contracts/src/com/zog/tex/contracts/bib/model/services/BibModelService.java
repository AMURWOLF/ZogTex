package com.zog.tex.contracts.bib.model.services;

import java.io.InputStream;

import com.zog.tex.contracts.bib.model.entities.BibModel;
import com.zog.tex.contracts.util.nanoservice.Nanoservice;

/**
 * Nanoservice for parsing some InputStream into bibliographic registry model.
 */
public interface BibModelService extends Nanoservice {

	BibModel parse(InputStream input);
}
