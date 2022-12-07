package com.ebay.nst.tutorials.rest.servicewrapperstutorial;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.rest.NSTRestServiceWrapper;
import com.ebay.nst.schema.validation.NSTRestSchemaValidator;
import com.ebay.nst.tutorials.sharedtutorialutilities.rest.CanadaHoliday;
import com.ebay.service.protocol.http.NSTHttpRequest;
import com.ebay.service.protocol.http.NSTHttpRequestImpl;
import com.ebay.utility.service.ServiceUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServiceWrappersWrapper implements NSTRestServiceWrapper {

    // The following are defined as constants as they are re-used in various interface methods.
    private static final String SERVICE_NAME = "canadaholidays";
    private static final String ENDPOINT = "/api/v1/holidays/{holidayId}";
    private final CanadaHoliday canadaHoliday;

    public ServiceWrappersWrapper(CanadaHoliday canadaHoliday) {
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
        return new NSTHttpRequestImpl.Builder(url, NstRequestType.GET)
                .setHeaders(headers)
                .build();
    }

    @Override
    public NSTRestSchemaValidator getSchemaValidator() {
        return null;
    }

}
