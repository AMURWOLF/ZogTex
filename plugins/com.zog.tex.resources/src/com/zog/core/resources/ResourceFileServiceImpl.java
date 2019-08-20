package com.zog.core.resources;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.base.Preconditions;
import com.zog.core.contracts.resources.exceptions.ResourceNotFoundException;
import com.zog.core.contracts.resources.nanoservices.ResourceFileService;
import com.zog.core.osgi.bundles.BundleNotFoundException;

public class ResourceFileServiceImpl implements ResourceFileService {

	@Override
	public File getFile(Class<?> classFromTargetBundle, String pathToFile) {
		Bundle bundle = FrameworkUtil.getBundle(classFromTargetBundle);
		if (bundle == null) {
			throw new BundleNotFoundException(classFromTargetBundle);
		}
		
		File result = null;
		URL fileURL = bundle.getResource(pathToFile);
		if (fileURL == null) {
			throw new ResourceNotFoundException(pathToFile, bundle);
		}
		try {
			URL resolvedFileURL = FileLocator.resolve(fileURL);
			result = new File(resolvedFileURL.toURI());
		} catch (URISyntaxException e1) {
		    e1.printStackTrace();
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		String msg = String.format("Resource with path \"%s\" not found!", pathToFile);
		Preconditions.checkNotNull(result, msg);
		Preconditions.checkArgument(result.exists(), msg);
		return result;
	}
}
