package com.zog.tex.integration.tests;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.zog.tex.contracts.bib.tokenization.enities.BibToken;
import com.zog.tex.contracts.bib.tokenization.enities.BibTokenType;
import com.zog.tex.contracts.bib.tokenization.services.BibTokenService;
import com.zog.tex.contracts.resources.services.ResourceStreamService;

public class IntegrTestBibTokenizingService {
	
	private BibTokenService bibTokenService;
	private ResourceStreamService resourceStreamService;
	
	@Before
	public void setup() {
		var thisClass = IntegrTestResourcesServices.class;
		
		var tokenServClass = BibTokenService.class;
		bibTokenService = TrackerUtil.getServiceByTracker(thisClass, tokenServClass);
		
		var streamServClass = ResourceStreamService.class;
		resourceStreamService = TrackerUtil.getServiceByTracker(thisClass, streamServClass);
	}
	
	@Test
	public void testToken() {
		InputStream bibFileStream = resourceStreamService.getStream("bib/testsrc.bib");
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
