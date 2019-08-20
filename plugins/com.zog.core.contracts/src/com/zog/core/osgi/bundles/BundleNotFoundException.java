package com.zog.core.osgi.bundles;

public class BundleNotFoundException extends RuntimeException {

	private static String msgTemplate = "Bundle that contains class \"%s\" not found in RunTime!";
	
	public BundleNotFoundException(Class<?> classInBundle) {
		super(String.format(msgTemplate, classInBundle.getName()));
	}
}
