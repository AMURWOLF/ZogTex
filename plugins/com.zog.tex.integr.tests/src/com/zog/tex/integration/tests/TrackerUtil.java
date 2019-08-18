package com.zog.tex.integration.tests;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.zog.tex.contracts.util.nanoservice.Nanoservice;

public class TrackerUtil {
	
	static <T extends Nanoservice> T getServiceByTracker(Class<?> integrTestClass, Class<T> nanoClass) {
		Bundle bundle = FrameworkUtil.getBundle(integrTestClass);
		if (bundle == null) {
			throw new RuntimeException("Bundle not found");
		}

		ServiceTracker<T, T> st = new ServiceTracker<>(bundle.getBundleContext(), nanoClass, null);
		st.open();
		try {
			// give the runtime some time to startup
			return st.waitForService(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
