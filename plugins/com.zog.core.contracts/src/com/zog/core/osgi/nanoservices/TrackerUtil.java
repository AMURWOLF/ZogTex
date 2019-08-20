package com.zog.core.osgi.nanoservices;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

public class TrackerUtil {
	
	public static <T extends Nanoservice> T getServiceByTracker(Class<?> collerClass, Class<T> nanoClass) {
		Bundle bundle = FrameworkUtil.getBundle(collerClass);
		if (bundle == null) {
			throw new RuntimeException("Bundle not found");
		}

		ServiceTracker<T, T> st = new ServiceTracker<>(bundle.getBundleContext(), nanoClass, null);
		st.open();
		T nanoservice = null;
		try {
			// give the runtime some time to startup
			nanoservice = st.waitForService(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (nanoservice == null) {
			throw new NanoserviceNotFoundException(nanoClass);
		}
		return nanoservice;
	}
}
