package com.zog.tex.integration.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.zog.tex.contracts.resources.services.ResourceFileService;
import com.zog.tex.contracts.resources.services.ResourceStreamService;

public class IntegrTestResourcesServices {

	private ResourceFileService fileService;
	private ResourceStreamService streamService;
	
	@Before
	public void setup() {
		var thisClass = IntegrTestResourcesServices.class;
		
		var fileServClass = ResourceFileService.class;
		fileService = TrackerUtil.getServiceByTracker(thisClass, fileServClass);
		
		var streamServClass = ResourceStreamService.class;
		streamService = TrackerUtil.getServiceByTracker(thisClass, streamServClass);
	}
	
	@Test
	public void testGetResourceFile() {
		File resource = fileService.getFile("bib/testsrc.bib");
		Assert.assertTrue(resource.exists());
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetResourceFileEmpty() {
		File resource = fileService.getFile("empty");
	}

	@Test
	public void testGetResourceStream() {
		try (InputStream resource = streamService.getStream("bib/testsrc.bib")) {
			Assert.assertTrue(resource.readAllBytes().length > 10);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetResourceStreamEmpty() {
		InputStream resource = streamService.getStream("empty");
	}
}
