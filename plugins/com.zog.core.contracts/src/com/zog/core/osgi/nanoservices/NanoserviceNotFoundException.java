package com.zog.core.osgi.nanoservices;

public class NanoserviceNotFoundException extends RuntimeException {
	
	private static String msgTemplate = "Nanoservice \"%s\" not found in RunTime!";
	
	public <T extends Nanoservice> NanoserviceNotFoundException(Class<T> nanoserviceInterfaceClass) {
		super(String.format(msgTemplate, nanoserviceInterfaceClass.getName()));
	}
}
