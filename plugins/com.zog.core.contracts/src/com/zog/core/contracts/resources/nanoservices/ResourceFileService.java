package com.zog.core.contracts.resources.nanoservices;

import java.io.File;

import com.zog.core.osgi.nanoservices.Nanoservice;

/**
 * Nanoservice for providing resources as files.
 */
public interface ResourceFileService extends Nanoservice {
	
	/**
	 * @param classFromTargetBundle Any class of bunlde that contains the resource.
	 * @param pathToFile Path to file. Resource root folder name is "/". 
	 * @return
	 */
	File getFile(Class<?> classFromTargetBundle, String pathToFile);
}
