package com.zog.tex.bib.model;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.zog.core.contracts.resources.nanoservices.ResourceStreamService;
import com.zog.core.osgi.nanoservices.NanoservicesRegistry;
import com.zog.tex.bib.contracts.model.services.BibModelService;
import com.zog.tex.bib.contracts.tokenization.services.BibTokenService;
import com.zog.tex.bib.model.services.BibModelServiceImpl;
import com.zog.tex.bib.tokenizing.services.BibTokenServiceImpl;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static NanoservicesRegistry nanoservicesRegistry;

	public static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
		nanoservicesRegistry = new NanoservicesRegistry(context);
		nanoservicesRegistry.ImportNanoservice(ResourceStreamService.class);
		nanoservicesRegistry.ExportNanoservice(BibTokenService.class, new BibTokenServiceImpl());
		nanoservicesRegistry.ImportNanoservice(BibTokenService.class);
		nanoservicesRegistry.ExportNanoservice(BibModelService.class, new BibModelServiceImpl());
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		context = null;
	}

	public static NanoservicesRegistry getNanoservicesRegistry() {
		return nanoservicesRegistry;
	}
}
