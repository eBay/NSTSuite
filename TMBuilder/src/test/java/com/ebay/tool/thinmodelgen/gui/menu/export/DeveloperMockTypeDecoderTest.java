package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.service.protocol.http.NSTHttpResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

public class DeveloperMockTypeDecoderTest {

    @Test
    public void getBooleanMockTypeFromGeneric() {
        BooleanDeveloperMockType booleanMockType = new BooleanDeveloperMockType();
        DeveloperMockType mockType = booleanMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.BOOLEAN)));
    }

    @Test
    public void getDoubleMockTypeFromGeneric() {
        DoubleDeveloperMockType doubleMockType = new DoubleDeveloperMockType();
        DeveloperMockType mockType = doubleMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.DOUBLE)));
    }

    @Test
    public void getIntegerMockTypeFromGeneric() {
        IntegerDeveloperMockType intMockType = new IntegerDeveloperMockType();
        DeveloperMockType mockType = intMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.INTEGER)));
    }

    @Test
    public void getStringMockTypeFromGeneric() {
        StringDeveloperMockType stringMockType = new StringDeveloperMockType();
        DeveloperMockType mockType = stringMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.STRING)));
    }

    @Test
    public void getUnknownMockTypeFromGeneric() {
        UnknownDeveloperMockType unknownDeveloperMockType = new UnknownDeveloperMockType();
        DeveloperMockType mockType = unknownDeveloperMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.UNKNOWN)));
    }

    // Test classes that implement DeveloperMockTypeDecoder with the various primitive box types.
    private class BooleanDeveloperMockType implements DeveloperMockTypeDecoder<Boolean> {

    }

    private class DoubleDeveloperMockType implements DeveloperMockTypeDecoder<Double> {

    }

    private class IntegerDeveloperMockType implements DeveloperMockTypeDecoder<Integer> {

    }

    private class StringDeveloperMockType implements DeveloperMockTypeDecoder<String> {

    }

    private class UnknownDeveloperMockType implements DeveloperMockTypeDecoder<Object> {

    }
}