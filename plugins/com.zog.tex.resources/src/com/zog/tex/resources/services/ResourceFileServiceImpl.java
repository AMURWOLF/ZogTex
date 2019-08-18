package com.zog.tex.resources.services;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.google.common.base.Preconditions;
import com.zog.tex.contracts.resources.services.ResourceFileService;
import com.zog.tex.resources.Activator;

public class ResourceFileServiceImpl implements ResourceFileService {

	@Override
	public File getFile(String pathToFile) {
		BundleContext context = Activator.getContext();
		File result = null;
		if (context != null) {
			Bundle bundle = context.getBundle();
			URL fileURL = bundle.getResource(pathToFile);
			try {
				URL resolvedFileURL = FileLocator.resolve(fileURL);
				result = new File(resolvedFileURL.toURI());
			} catch (URISyntaxException e1) {
			    e1.printStackTrace();
			} catch (IOException e1) {
			    e1.printStackTrace();
			}
		} else {
			throw new RuntimeException("Bundle not started!");
		}
		String msg = String.format("Resource with path \"%s\" not found!", pathToFile);
		Preconditions.checkNotNull(result, msg);
		Preconditions.checkArgument(result.exists(), msg);
		return result;
	}
}
