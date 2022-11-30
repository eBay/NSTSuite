package com.ebay.service.protocol.http;

import java.net.URL;
import java.util.Map;

import com.ebay.nst.NstRequestType;

/**
 * Values returned MUST NEVER be null.
 */
public interface NSTHttpRequest {

	/**
	 * Get the request type to send.
	 * 
	 * @return Request type to send.
	 */
	public NstRequestType getRequestType();

	/**
	 * Get the url.
	 * 
	 * @return Url to be called.
	 */
	public URL getUrl();

	/**
	 * Get the headers to send with the request.
	 * 
	 * @return Headers to send.
	 */
	public Map<String, String> getHeaders();

	/**
	 * Get the request payload to send.
	 * 
	 * @return Request payload.
	 */
	public String getPayload();

	/**
	 * Get the call timeout in milliseconds. 0 signifies indefinite timeout.
	 * 
	 * @return Timeout in milliseconds..
	 */
	public int getTimeoutInMilliseconds();

	/**
	 * Get the trust all SSL certs flag value. For certs not properly registered the
	 * cert check will fail. By trusting all certs it is possible to bypass this
	 * failure point with the service.
	 * 
	 * @return True to trust all certs, false otherwise.
	 */
	public boolean getTrustAllCerts();
}
