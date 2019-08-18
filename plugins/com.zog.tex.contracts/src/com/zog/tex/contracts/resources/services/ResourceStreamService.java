package com.zog.tex.contracts.resources.services;

import java.io.InputStream;

import com.zog.tex.contracts.util.nanoservice.Nanoservice;

/**
 * Nanoservice for providing resources as input streams.
 */
public interface ResourceStreamService extends Nanoservice {

	InputStream getStream(String pathToFile);
}
