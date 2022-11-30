package com.ebay.utility.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.ebay.nst.NSTServiceWrapper;
import com.ebay.nst.hosts.manager.HostsManager;
import com.ebay.nst.schema.validation.NSTSchemaValidator;

public class ServiceUtil {

	/**
	 * Get the complete URL from the service wrapper definition.
	 * 
	 * @param serviceWrapper Service wrapper definition.
	 * @return Complete URL.
	 */
	public static URL getUrl(NSTServiceWrapper<? extends NSTSchemaValidator> serviceWrapper) {

		HostsManager hostsManager;
		try {
			hostsManager = HostsManager.getInstance();
		} catch (IllegalStateException | URISyntaxException | IOException e) {
			throw new RuntimeException("HostsManager was unable to load hosts file.", e);
		}
		String host = hostsManager.getHostForService(serviceWrapper.getServiceName());
		if (serviceWrapper.getEndpointPath() == null) {
			throw new IllegalStateException("NSTServiceWrapper endpoint path was null. It cannot be null.");
		}
		
		URL url;
		try {
			url = new URL(host + serviceWrapper.getEndpointPath());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL exception.", e);
		}
		
		return url;
	}
}
