package com.zog.tex.integration.tests;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.zog.core.contracts.resources.nanoservices.ResourceStreamService;
import com.zog.core.osgi.nanoservices.TrackerUtil;
import com.zog.tex.bib.contracts.tokenization.entities.BibToken;
import com.zog.tex.bib.contracts.tokenization.entities.BibTokenType;
import com.zog.tex.bib.contracts.tokenization.services.BibTokenService;

public class IntegrTestBibTokenizingService {
	
	private BibTokenService bibTokenService;
	private ResourceStreamService resourceStreamService;
	private static String pathToBibFile = "bib/testsrc.bib";
	
	@Before
	public void setup() {
		var tokenServClass = BibTokenService.class;
		bibTokenService = TrackerUtil.getServiceByTracker(this.getClass(), tokenServClass);
		
		var streamServClass = ResourceStreamService.class;
		resourceStreamService = TrackerUtil.getServiceByTracker(this.getClass(), streamServClass);
	}
	
	@Test
	public void testToken() {
		InputStream bibFileStream = resourceStreamService.getStream(this.getClass(), pathToBibFile);
		Iterable<BibToken> bibTokens = bibTokenService.tokenize(bibFileStream);

		Iterator<BibToken> i = bibTokens.iterator();
		BibToken bibToken = i.next();
		assertEquals("inproceedings", bibToken.getContent());
		assertEquals(BibTokenType.ENTRY_TYPE, bibToken.getType());
		assertEquals(1, bibToken.getFrom());

		bibToken = i.next();
		assertEquals("dremel", bibToken.getContent());
		assertEquals(BibTokenType.CITE_KEY, bibToken.getType());
		assertEquals(15, bibToken.getFrom());
		
		bibToken = i.next();
		assertEquals("title", bibToken.getContent());
		assertEquals(BibTokenType.PROP_KEY, bibToken.getType());
		assertEquals(24, bibToken.getFrom());
		
		bibToken = i.next();
		assertEquals("{Dremel: Interactive Analysis of Web-Scale Datasets}", bibToken.getContent());
		assertEquals(BibTokenType.PROP_VAL, bibToken.getType());
		assertEquals(33, bibToken.getFrom());
		
		bibToken = i.next();
		assertEquals("author", bibToken.getContent());
		bibToken = i.next();
		assertEquals(
				"Sergey Melnik and Andrey Gubarev and Jing Jing Long and Geoffrey Romer and Shiva Shivakumar and Matt Tolton and Theo Vassilakis",
				bibToken.getContent());
		bibToken = i.next();
		assertEquals("year", bibToken.getContent());
		bibToken = i.next();
		assertEquals("2010", bibToken.getContent());
	}
}
