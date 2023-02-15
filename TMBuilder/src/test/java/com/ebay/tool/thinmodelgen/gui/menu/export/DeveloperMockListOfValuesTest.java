package com.ebay.tool.thinmodelgen.gui.menu.export;

import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DeveloperMockListOfValuesTest {

    @Test
    public void getBooleanMockTypeFromGeneric() {
        DeveloperMockListOfBoolean booleanMockType = new DeveloperMockListOfBoolean();
        DeveloperMockType mockType = booleanMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.LIST_OF_BOOLEAN)));
    }

    @Test
    public void getDoubleMockTypeFromGeneric() {
        DeveloperMockListOfDouble doubleMockType = new DeveloperMockListOfDouble();
        DeveloperMockType mockType = doubleMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.LIST_OF_DOUBLE)));
    }

    @Test
    public void getIntegerMockTypeFromGeneric() {
        DeveloperMockListOfInteger intMockType = new DeveloperMockListOfInteger();
        DeveloperMockType mockType = intMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.LIST_OF_INTEGER)));
    }

    @Test
    public void getStringMockTypeFromGeneric() {
        DeveloperMockListOfString stringMockType = new DeveloperMockListOfString();
        DeveloperMockType mockType = stringMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.LIST_OF_STRING)));
    }

    @Test
    public void getUnknownMockTypeFromGeneric() {
        DeveloperMockListOfObject unknownDeveloperMockType = new DeveloperMockListOfObject();
        DeveloperMockType mockType = unknownDeveloperMockType.getMockType();
        assertThat(mockType, is(equalTo(DeveloperMockType.UNKNOWN)));
    }

    // Test classes that implement DeveloperMockTypeDecoder with the various primitive box types.
    private class DeveloperMockListOfBoolean implements DeveloperMockListOfValues<Boolean> {

        @Override
        public List<Boolean> getMockValues() {
            return null;
        }

        @Override
        public void setMockValues(List<Boolean> value) {

        }
    }

    private class DeveloperMockListOfDouble implements DeveloperMockListOfValues<Double> {

        @Override
        public List<Double> getMockValues() {
            return null;
        }

        @Override
        public void setMockValues(List<Double> value) {

        }
    }

    private class DeveloperMockListOfInteger implements DeveloperMockListOfValues<Integer> {

        @Override
        public List<Integer> getMockValues() {
            return null;
        }

        @Override
        public void setMockValues(List<Integer> value) {

        }
    }

    private class DeveloperMockListOfString implements DeveloperMockListOfValues<String> {

        @Override
        public List<String> getMockValues() {
            return null;
        }

        @Override
        public void setMockValues(List<String> value) {

        }
    }

    private class DeveloperMockListOfObject implements DeveloperMockListOfValues<Object> {

        @Override
        public List<Object> getMockValues() {
            return null;
        }

        @Override
        public void setMockValues(List<Object> value) {

        }
    }
}