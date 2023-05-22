package com.ebay.service.logger.har;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.*;

public class HarRequestTest {

    @Test
    public void authHeaderObfuscated() {

        Header authorizationUpperCase = new Header();
        authorizationUpperCase.setName("Authorization");
        authorizationUpperCase.setValue("Bearer 12345qwert");

        Header authorizationLowerCase = new Header();
        authorizationLowerCase.setName("authorization");
        authorizationLowerCase.setValue("Bearer 12345qwert");

        Header accept = new Header();
        accept.setName("Accept");
        accept.setValue("application/json");

        Header contentType = new Header();
        contentType.setName("Content-Type");
        contentType.setValue("application/json");

        List<Header> inputHeaders = new ArrayList<>();
        inputHeaders.add(authorizationUpperCase);
        inputHeaders.add(authorizationLowerCase);
        inputHeaders.add(accept);
        inputHeaders.add(contentType);

        HarRequest request = new HarRequest();
        request.setHeaders(inputHeaders);
        List<Header> actualHeaders = request.getHeaders();

        Header authorizationUpperCaseExpected = new Header();
        authorizationUpperCaseExpected.setName("Authorization");
        authorizationUpperCaseExpected.setValue("Bearer v^obfuscated123");

        Header authorizationLowerCaseExpected = new Header();
        authorizationLowerCaseExpected.setName("authorization");
        authorizationLowerCaseExpected.setValue("Bearer v^obfuscated123");

        List<Header> expectedHeaders = new ArrayList<>();
        expectedHeaders.add(authorizationUpperCaseExpected);
        expectedHeaders.add(authorizationLowerCaseExpected);
        expectedHeaders.add(accept);
        expectedHeaders.add(contentType);

        assertThat(actualHeaders, is(equalTo(expectedHeaders)));
    }
}