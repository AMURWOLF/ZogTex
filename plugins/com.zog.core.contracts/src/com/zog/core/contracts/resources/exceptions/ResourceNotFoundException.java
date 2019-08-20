package com.zog.core.contracts.resources.exceptions;

import org.osgi.framework.Bundle;

public class ResourceNotFoundException extends RuntimeException {

	static String msgTemplate = "Resource with path \"%s\" not found in bundle \"%s\"!";
	
	public ResourceNotFoundException(String resourcePath, Bundle targetBundle) {
		super(String.format(msgTemplate, resourcePath, targetBundle.getSymbolicName()));
	}
}
