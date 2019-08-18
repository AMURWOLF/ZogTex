package com.zog.tex.bib.model.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.zog.tex.bib.model.Activator;
import com.zog.tex.contracts.bib.model.entities.BibEntry;
import com.zog.tex.contracts.bib.model.entities.BibModel;
import com.zog.tex.contracts.bib.model.services.BibModelService;
import com.zog.tex.contracts.bib.tokenization.enities.BibToken;
import com.zog.tex.contracts.bib.tokenization.services.BibTokenService;

public class BibModelServiceImpl implements BibModelService {

	private BibTokenService bibTokenService;

	public BibModelServiceImpl() {
		bibTokenService = Activator.getNanoservicesRegistry().getNanoservice(BibTokenService.class);
	}

	@Override
	public BibModel parse(InputStream input) {
		BibModel model = new BibModel();
		BibEntry entry = null;
		String bibPropType = null;
		String bibPropValue = null;
		for (BibToken bibToken : bibTokenService.tokenize(input)) {
			switch (bibToken.getType()) {
			case ENTRY_TYPE:
				if (null != entry) {
					model.addEntry(entry);
				}
				entry = new BibEntry();
				entry.setType(bibToken.getContent());
				break;
			case CITE_KEY:
				entry.setCiteKey(bibToken.getContent());
				break;
			case PROP_KEY:
				bibPropType = bibToken.getContent();
				break;
			case PROP_VAL:
				bibPropValue = bibToken.getContent();
				entry.addProperty(bibPropType, bibPropValue);
				bibPropType = null;
				bibPropValue = null;
				break;
			}
		}
		if (null != entry) {
			model.addEntry(entry);
		}
		try {
			input.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return model;
	}

	@Override
	public BibModel parse(String input) {
		InputStream textStream = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));
		return parse(textStream);
	}
}
