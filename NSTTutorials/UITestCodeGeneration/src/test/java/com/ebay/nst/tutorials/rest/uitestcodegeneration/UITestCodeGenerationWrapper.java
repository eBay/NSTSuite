package com.ebay.nst.tutorials.rest.uitestcodegeneration;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.rest.NSTRestServiceWrapper;
import com.ebay.nst.schema.validation.NSTRestSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.AllowAdditionalProperties;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.StatusCode;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.ebay.utility.service.ServiceUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UITestCodeGenerationWrapper implements NSTRestServiceWrapper {

    // The following are defined as constants as they are re-used in various interface methods.
    private static final String SERVICE_NAME = "canadaholidays";
    private static final String ENDPOINT = "/api/v1/holidays/{holidayId}";
    private static final NstRequestType NST_REQUEST_TYPE = NstRequestType.GET;
    private final CanadaHoliday canadaHoliday;

    public UITestCodeGenerationWrapper(CanadaHoliday canadaHoliday) {
        this.canadaHoliday = Objects.requireNonNull(canadaHoliday);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public NstRequestType getRequestType() {
        return NstRequestType.GET;
    }

    @Override
    public String getEndpointPath() {
        return ENDPOINT.replace("{holidayId}", canadaHoliday.getHolidayId().toString());
    }

    @Override
    public NSTHttpRequest prepareRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("USER-AGENT", "testHeader");

        URL url = ServiceUtil.getUrl(this);
        return new NSTHttpRequestImpl.Builder(url, NST_REQUEST_TYPE)
                .setHeaders(headers)
                .build();
    }

    @Override
    public NSTRestSchemaValidator getSchemaValidator() {
        return new OpenApiSchemaValidator.Builder(
                "canada-holidays.yaml",
                ENDPOINT,
                NST_REQUEST_TYPE)
                .allowAdditionalProperties(AllowAdditionalProperties.YES)
                // Payloads must be set in the case of PUT/POST requests.
                // .setPayload(null)
                .setStatusCode(StatusCode._200)
                .build();
    }

}
