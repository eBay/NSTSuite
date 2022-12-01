package com.nst.tutorials.rest.contractvalidations;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.hosts.manager.HostsManager;
import com.ebay.nst.rest.NSTRestServiceWrapper;
import com.ebay.nst.schema.validation.NSTRestSchemaValidator;
import com.ebay.nst.schema.validation.OpenApiSchemaValidator;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.nst.tutorials.rest.CanadaHoliday;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContractValidationsPolymorphicErrorWrapper implements NSTRestServiceWrapper {

    private static final String SERVICE_NAME = "canadaholidays";
    private static final String ENDPOINT = "/api/v1/holidays/{holidayId}";
    private static final NstRequestType NST_REQUEST_TYPE = NstRequestType.GET;
    private final CanadaHoliday canadaHoliday;

    public ContractValidationsPolymorphicErrorWrapper(CanadaHoliday canadaHoliday) {
        this.canadaHoliday = Objects.requireNonNull(canadaHoliday);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public NstRequestType getRequestType() {
        return NST_REQUEST_TYPE;
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
            String path = HostsManager.getInstance().getHostForService(SERVICE_NAME) + ENDPOINT;
            url = new URL(path.replace("{holidayId}", canadaHoliday.getHolidayId().toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new NSTHttpRequestImpl.Builder(url, NstRequestType.GET)
                .setHeaders(headers)
                .build();
    }

    @Override
    public NSTRestSchemaValidator getSchemaValidator() {
        return new OpenApiSchemaValidator.Builder(
                "com/nst/tutorials/rest/canada-holidays-polymorphic-error.yaml",
                ENDPOINT,
                NST_REQUEST_TYPE)
                .allowAdditionalProperties(OpenApiSchemaValidator.AllowAdditionalProperties.NO)
                .build();
    }

}
