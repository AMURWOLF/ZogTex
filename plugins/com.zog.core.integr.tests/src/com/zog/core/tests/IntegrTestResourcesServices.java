package com.zog.core.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zog.core.contracts.resources.exceptions.ResourceNotFoundException;
import com.zog.core.contracts.resources.nanoservices.ResourceFileService;
import com.zog.core.contracts.resources.nanoservices.ResourceStreamService;
import com.zog.core.osgi.nanoservices.TrackerUtil;

public class IntegrTestResourcesServices {

	private ResourceFileService fileService;
	private ResourceStreamService streamService;
	private static String pathToResource = "someFolder/someResourceFile.txt";
	
	@Before
	public void setup() {
		var fileServClass = ResourceFileService.class;
		fileService = TrackerUtil.getServiceByTracker(this.getClass(), fileServClass);
		
		var streamServClass = ResourceStreamService.class;
		streamService = TrackerUtil.getServiceByTracker(this.getClass(), streamServClass);
	}
	
	@Test
	public void testGetResourceFile() {
		File resource = fileService.getFile(this.getClass(), pathToResource);
		Assert.assertTrue(resource.exists());
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void testGetResourceFileEmpty() {
		File resource = fileService.getFile(this.getClass(), "empty");
	}

	@Test
	public void testGetResourceStream() {
		try (InputStream resource = streamService.getStream(this.getClass(), pathToResource)) {
			Assert.assertTrue(resource.readAllBytes().length == 9);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void testGetResourceStreamEmpty() {
		InputStream resource = streamService.getStream(this.getClass(), "empty");
	}
}
