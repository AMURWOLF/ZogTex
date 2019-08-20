package com.zog.core.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.common.base.Preconditions;
import com.zog.core.contracts.resources.exceptions.ResourceNotFoundException;
import com.zog.core.contracts.resources.nanoservices.ResourceStreamService;
import com.zog.core.osgi.bundles.BundleNotFoundException;

public class ResourceStreamServiceImpl implements ResourceStreamService {

	@Override
	public InputStream getStream(Class<?> classFromTargetBundle, String pathToFile) {
		Bundle bundle = FrameworkUtil.getBundle(classFromTargetBundle);
		if (bundle == null) {
			throw new BundleNotFoundException(classFromTargetBundle);
		}
		
		final InputStream result;
		URL fileURL = bundle.getResource(pathToFile);
		if (fileURL == null) {
			throw new ResourceNotFoundException(pathToFile, bundle);
		}
		InputStream stream = null;
		try {
			URL resolvedFileURL = FileLocator.resolve(fileURL);
			result = resolvedFileURL.openStream();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
		String msg = String.format("Resource with path \"%s\" not found!", pathToFile);
		Preconditions.checkNotNull(result, msg);
		return result;
	}
}
