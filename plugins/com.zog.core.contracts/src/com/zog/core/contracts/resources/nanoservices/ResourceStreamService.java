package com.zog.core.contracts.resources.nanoservices;

import java.io.InputStream;

import com.zog.core.osgi.nanoservices.Nanoservice;

/**
 * Nanoservice for providing resources as input streams.
 */
public interface ResourceStreamService extends Nanoservice {

	/**
	 * @param classFromTargetBundle Any class of bunlde that contains the resource.
	 * @param pathToFile Path to file. Resource root folder name is "/". 
	 * @return
	 */
	InputStream getStream(Class<?> classFromTargetBundle, String pathToFile);
}
