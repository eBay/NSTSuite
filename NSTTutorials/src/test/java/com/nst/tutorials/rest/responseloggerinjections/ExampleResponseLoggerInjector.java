package com.nst.tutorials.rest.responseloggerinjections;

import com.ebay.service.logger.injection.ResponseLoggerInjector;
import org.json.JSONObject;

/**
 * Most cases do not require manipulating the response before recording the
 * mock. Defining an injector class, like shown below, allows you to modify the
 * response payload before it is written to file.
 */
public class ExampleResponseLoggerInjector implements ResponseLoggerInjector {

	@Override
	public String processServiceResponse(String rawServiceResponsePayload) {

		// Change the holiday date field to "MODIFIED DATE FIELD VALUE"
		// Path: $.holiday.date

		JSONObject holiday = new JSONObject(rawServiceResponsePayload).getJSONObject("holiday");
		holiday.put("date", "MODIFIED DATE FIELD VALUE");

		return holiday.toString();
	}

}
