package com.zog.tex.contracts.util.nanoservice;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Helper for importing, storing and exporting nanoservices.
 */
public class NanoservicesRegistry {

	private Map<Class<? extends Nanoservice>, Nanoservice> servicesMap = new HashMap<>();
	private BundleContext context;

	public NanoservicesRegistry(BundleContext context) {
		this.context = context;
	}

	public void ImportNanoservice(Class<? extends Nanoservice> contract) {
		ServiceReference<? extends Nanoservice> reference = context.getServiceReference(contract);
		Nanoservice realization = context.getService(reference);
		servicesMap.put(contract, realization);
	}

	public <T extends Nanoservice> void ExportNanoservice(Class<T> contract, T realization) {
		context.registerService(contract, realization, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Nanoservice> T getNanoservice(Class<T> Clazz) {
		Nanoservice service = servicesMap.get(Clazz);
		if (service == null) {
			String msg = String.format("Service \"%s\" not loaded!", Clazz.getSimpleName());
			throw new NullPointerException(msg);
		}
		return (T) service;
	}
}
