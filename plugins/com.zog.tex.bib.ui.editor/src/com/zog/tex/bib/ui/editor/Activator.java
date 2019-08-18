package com.zog.tex.bib.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.zog.tex.contracts.bib.model.services.BibModelService;
import com.zog.tex.contracts.bib.tokenization.services.BibTokenService;
import com.zog.tex.contracts.resources.services.ResourceStreamService;
import com.zog.tex.contracts.util.nanoservice.NanoservicesRegistry;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.zog.tex.bib.ui.editor"; //$NON-NLS-1$
	private static Activator plugin;
	private static BundleContext context;
	private static NanoservicesRegistry nanoservicesRegistry;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Activator.context = context;
		nanoservicesRegistry = new NanoservicesRegistry(context);
		nanoservicesRegistry.ImportNanoservice(ResourceStreamService.class);
		nanoservicesRegistry.ImportNanoservice(BibTokenService.class);
		nanoservicesRegistry.ImportNanoservice(BibModelService.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static BundleContext getContext() {
		return context;
	}
	
	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static NanoservicesRegistry getNanoservicesRegistry() {
		return nanoservicesRegistry;
	}
}
