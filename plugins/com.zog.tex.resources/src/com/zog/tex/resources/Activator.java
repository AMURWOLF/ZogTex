package com.zog.tex.resources;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.zog.tex.contracts.resources.services.ResourceFileService;
import com.zog.tex.contracts.resources.services.ResourceStreamService;
import com.zog.tex.contracts.util.nanoservice.NanoservicesRegistry;
import com.zog.tex.resources.services.ResourceFileServiceImpl;
import com.zog.tex.resources.services.ResourceStreamServiceImpl;

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
		nanoservicesRegistry.ExportNanoservice(ResourceFileService.class, new ResourceFileServiceImpl());
		nanoservicesRegistry.ExportNanoservice(ResourceStreamService.class, new ResourceStreamServiceImpl());
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		context = null;
	}
	
	public static NanoservicesRegistry getNanoservicesRegistry() {
		return nanoservicesRegistry;
	}
}
