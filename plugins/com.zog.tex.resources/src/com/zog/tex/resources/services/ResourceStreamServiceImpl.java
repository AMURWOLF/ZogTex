package com.zog.tex.resources.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.google.common.base.Preconditions;
import com.zog.tex.contracts.resources.services.ResourceStreamService;
import com.zog.tex.resources.Activator;

public class ResourceStreamServiceImpl implements ResourceStreamService {

	@Override
	public InputStream getStream(String pathToFile) {
		BundleContext context = Activator.getContext();
		final InputStream result;
		if (context != null) {
			Bundle bundle = context.getBundle();
			URL fileURL = bundle.getResource(pathToFile);
			InputStream stream = null;
			try {
				URL resolvedFileURL = FileLocator.resolve(fileURL);
				result = resolvedFileURL.openStream();
			} catch (IOException e) {
			    throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Bundle not started!");
		}
		String msg = String.format("Resource with path \"%s\" not found!", pathToFile);
		Preconditions.checkNotNull(result, msg);
		return result;
	}
}
