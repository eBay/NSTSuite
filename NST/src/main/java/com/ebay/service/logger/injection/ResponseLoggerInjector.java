package com.ebay.service.logger.injection;

/**
 * Adopt this interface to massage the service response before it is written to
 * file as the mock.
 *
 * This should be used with extreme caution. You are modifying the mock that
 * will be used for all platform testing.
 *
 * Example uses of this is to replace URL fields with a URL that will shortcut
 * web flows that are outside native control. EG: Co-branded card must complete
 * the Synchrony web applicaiton flow to approve new users. By injecting the URL
 * the web view looks for to know that the application was successful we can
 * force the web flow to move past the applicaiton flow without having to
 * navigate through it on the native client.
 *
 * NOTE: This will only allow you to modify the data written to file. This does
 * NOT modify the response object that is validated against or handed back as
 * the JSONObject.
 *
 * @author byarger
 *
 */
public interface ResponseLoggerInjector {

  /**
   * Receive the raw service response and modify it before writing to file. This
   * will NOT modify the response object returned by the service.
   *
   * @param rawServiceResponsePayload Raw service payload to modify as deemed necessary before writing to file.
   * @return Modified response string.
   */
  public String processServiceResponse(String rawServiceResponsePayload);
}
