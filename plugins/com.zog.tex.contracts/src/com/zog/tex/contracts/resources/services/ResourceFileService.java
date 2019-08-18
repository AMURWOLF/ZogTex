package com.zog.tex.contracts.resources.services;

import java.io.File;

import com.zog.tex.contracts.util.nanoservice.Nanoservice;

/**
 * Nanoservice for providing resources as files.
 */
public interface ResourceFileService extends Nanoservice {
	
	File getFile(String pathToFile);
}
