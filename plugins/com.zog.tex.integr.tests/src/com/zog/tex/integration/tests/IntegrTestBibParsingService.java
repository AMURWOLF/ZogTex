package com.zog.tex.integration.tests;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;

import com.zog.tex.contracts.bib.model.entities.BibEntry;
import com.zog.tex.contracts.bib.model.entities.BibModel;
import com.zog.tex.contracts.bib.model.services.BibModelService;
import com.zog.tex.contracts.resources.services.ResourceStreamService;

public class IntegrTestBibParsingService {

	private ResourceStreamService resourceStreamService;
	private BibModelService parsingService;
	
	@Before
	public void setup() {
		var thisClass = IntegrTestResourcesServices.class;
		
		var streamServClass = ResourceStreamService.class;
		resourceStreamService = TrackerUtil.getServiceByTracker(thisClass, streamServClass);
		
		var parseServClass = BibModelService.class;
		parsingService = TrackerUtil.getServiceByTracker(thisClass, parseServClass);		
	}
	
	@Test
	public void testParse() throws Exception {
		InputStream bibFileStream = resourceStreamService.getStream("bib/testsrc.bib");
		BibModel model = parsingService.parse(bibFileStream);

		assertEquals(2, model.getEntries().size());

		BibEntry entry = model.getEntries().get(0);
		assertEquals("inproceedings", entry.getType());
		assertEquals("dremel", entry.getCiteKey());
		assertEquals("{Dremel: Interactive Analysis of Web-Scale Datasets}", entry.getProperty("title"));
		assertEquals(
				"Sergey Melnik and Andrey Gubarev and Jing Jing Long and Geoffrey Romer and Shiva Shivakumar and Matt Tolton and Theo Vassilakis",
				entry.getProperty("author"));
		assertEquals("2010", entry.getProperty("year"));
		assertEquals("http://www.vldb2010.org/accept.htm", entry.getProperty("URL"));
		assertEquals("Proc. of the 36th Int'l Conf on Very Large Data Bases", entry.getProperty("booktitle"));
		assertEquals("330-339", entry.getProperty("pages"));

		entry = model.getEntries().get(1);
		assertEquals("inproceedings", entry.getType());
		assertEquals("parallel_sm_gpu", entry.getCiteKey());
	}
}
