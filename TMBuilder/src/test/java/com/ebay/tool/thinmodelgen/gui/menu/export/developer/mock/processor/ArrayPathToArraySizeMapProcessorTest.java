package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import org.hamcrest.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class ArrayPathToArraySizeMapProcessorTest {

    @Test
    public void populateArrayPathToArraySizeMap_WithoutAnyArrayPaths() throws Exception {

        NodeModel objectModel = TestUtility.getListOfStringNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel[] nodeModels = new NodeModel[] {objectModel};

        ValidationSetModel validationSetModel = mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        TreeMap<String, Integer> actual = ArrayPathToArraySizeMapProcessor.getArrayPathToArraySizeMap(validationSetModel);
        assertThat("ArrayPathToArraySizeMap MUST be empty.", actual, is(anEmptyMap()));
    }

    @Test
    public void populateArrayPathToArraySizeMap() throws Exception {

        NodeModel firstModel = TestUtility.getListOfIntegerNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel secondModel = TestUtility.getListOfStringNodeModel("testStringObject", "$.first.second.third[*].name", new ArrayList<>());
        NodeModel thirdModel = TestUtility.getListOfStringNodeModel("secondTestStringObject", "$.first.second.third[5].name", new ArrayList<>());
        NodeModel fourthModel = TestUtility.getListOfStringNodeModel("thirdTestStringObject", "$.first.second.third[*].address[3].city", new ArrayList<>());
        NodeModel fifthModel = TestUtility.getListOfStringNodeModel("fourthTestStringObject", "$.first.second.third[3].name", new ArrayList<>());

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel, fourthModel, fifthModel};

        ValidationSetModel validationSetModel = mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        TreeMap<String, Integer> actual = ArrayPathToArraySizeMapProcessor.getArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> expected = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expected.put("$.first.second.third", 6);
        expected.put("$.first.second.third.address", 4);
        assertThat("ArrayPathToArraySizeMap MUST match expected.", actual, is(equalTo(expected)));
    }

    @Test
    public void populateArrayPathToArraySizeMapUsingLargerListOfMockValues() throws Exception {

        NodeModel firstModel = TestUtility.getListOfIntegerNodeModel("testObject", "$.first.second", new ArrayList<>());
        NodeModel secondModel = TestUtility.getListOfStringNodeModel("testStringObject", "$.first.second.third[2].name", new ArrayList<>());
        NodeModel thirdModel = TestUtility.getListOfStringNodeModel("secondTestStringObject", "$.first.second.third[*].name", new ArrayList<>(Arrays.asList("one", "two", "three", "four")));

        NodeModel[] nodeModels = new NodeModel[] {firstModel, secondModel, thirdModel};

        ValidationSetModel validationSetModel = mock(ValidationSetModel.class);
        when(validationSetModel.getData()).thenReturn(nodeModels);

        TreeMap<String, Integer> actual = ArrayPathToArraySizeMapProcessor.getArrayPathToArraySizeMap(validationSetModel);
        TreeMap<String, Integer> expected = new TreeMap<>(new SmallestToLargestArrayPathComparator());
        expected.put("$.first.second.third", 4);
        assertThat("ArrayPathToArraySizeMap MUST match expected.", actual, is(equalTo(expected)));
    }
}