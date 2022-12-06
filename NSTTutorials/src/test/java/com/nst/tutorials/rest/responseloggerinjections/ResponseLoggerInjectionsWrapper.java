package com.nst.tutorials.rest.responseloggerinjections;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.hosts.manager.HostsManager;
import com.ebay.nst.rest.NSTRestServiceWrapper;
import com.ebay.nst.schema.validation.NSTRestSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.AllowAdditionalProperties;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator.StatusCode;
import com.ebay.service.logger.injection.ResponseLoggerInjector;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.nst.tutorials.rest.shared.CanadaHoliday;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResponseLoggerInjectionsWrapper implements NSTRestServiceWrapper {

    // The following are defined as constants as they are re-used in various interface methods.
    private static final String SERVICE_NAME = "canadaholidays";
    private static final String ENDPOINT = "/api/v1/holidays/{holidayId}";
    private static final NstRequestType NST_REQUEST_TYPE = NstRequestType.GET;
    private final CanadaHoliday canadaHoliday;

    public ResponseLoggerInjectionsWrapper(CanadaHoliday canadaHoliday) {
        this.canadaHoliday = Objects.requireNonNull(canadaHoliday);
    }

    // Add the response logger injector to the service wrapper to enable it
    @Override
    public ResponseLoggerInjector getResponseLoggerInjector() {
        return new ExampleResponseLoggerInjector();
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
        return ENDPOINT;
    }

    @Override
    public NSTHttpRequest prepareRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("USER-AGENT", "testHeader");

        URL url;
        try {
            // HostsManager will utilize the data read in from the serviceHosts.csv file, in the root resources directory.
            String path = HostsManager.getInstance().getHostForService(SERVICE_NAME) + ENDPOINT;
            url = new URL(path.replace("{holidayId}", canadaHoliday.getHolidayId().toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new NSTHttpRequestImpl.Builder(url, NST_REQUEST_TYPE)
                .setHeaders(headers)
                .build();
    }

    @Override
    public NSTRestSchemaValidator getSchemaValidator() {
        return new OpenApiSchemaValidator.Builder(
                "com/nst/tutorials/rest/canada-holidays.yaml",
                ENDPOINT,
                NST_REQUEST_TYPE)
                .allowAdditionalProperties(AllowAdditionalProperties.YES)
                // Payloads must be set in the case of PUT/POST requests.
                // .setPayload(null)
                .setStatusCode(StatusCode._200)
                .build();
    }

}
