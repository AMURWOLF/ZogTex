package com.zog.core;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.zog.core.contracts.resources.nanoservices.ResourceFileService;
import com.zog.core.contracts.resources.nanoservices.ResourceStreamService;
import com.zog.core.osgi.nanoservices.NanoservicesRegistry;
import com.zog.core.resources.ResourceFileServiceImpl;
import com.zog.core.resources.ResourceStreamServiceImpl;

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
