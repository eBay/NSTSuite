package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.*;
import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor.SmallestToLargestArrayPathComparator;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.*;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class DeveloperMockExportTest {

    DeveloperMockExport export;

    @BeforeMethod(alwaysRun = true)
    public void resetDeveloperMockExport() {
        export = new DeveloperMockExport();
    }

    @Test
    public void getCoreValidationSetByItselfAndCustomValidationSet() throws Exception {

        DeveloperMockExport exportMock = Mockito.spy(DeveloperMockExport.class);
        doNothing().when(exportMock).writeDeveloperMock(Mockito.any(File.class), Mockito.anyString(), Mockito.anyString());

        ValidationSetModel coreValidationSetModel = Mockito.mock(ValidationSetModel.class);
        when(coreValidationSetModel.getValidationSetName()).thenReturn(ExportConstants.CORE_VALIDATION_SET);
        when(coreValidationSetModel.getData()).thenReturn(new NodeModel[0]);

        ValidationSetModel customValidationSetModel = Mockito.mock(ValidationSetModel.class);
        when(customValidationSetModel.getValidationSetName()).thenReturn("FOO");
        when(customValidationSetModel.getData()).thenReturn(new NodeModel[0]);

        List<ValidationSetModel> validations = new ArrayList<>();
        validations.add(coreValidationSetModel);
        validations.add(customValidationSetModel);

        exportMock.export(new File(System.getProperty("user.dir")), validations);
        verify(exportMock, times(2)).writeDeveloperMock(Mockito.any(File.class), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void getCoreValidationSetByItself() throws Exception {

        DeveloperMockExport exportMock = Mockito.spy(DeveloperMockExport.class);
        doNothing().when(exportMock).writeDeveloperMock(Mockito.any(File.class), Mockito.anyString(), Mockito.anyString());

        ValidationSetModel coreValidationSetModel = Mockito.mock(ValidationSetModel.class);
        when(coreValidationSetModel.getValidationSetName()).thenReturn(ExportConstants.CORE_VALIDATION_SET);
        when(coreValidationSetModel.getData()).thenReturn(new NodeModel[0]);

        List<ValidationSetModel> validations = new ArrayList<>();
        validations.add(coreValidationSetModel);

        exportMock.export(new File(System.getProperty("user.dir")), validations);
        verify(exportMock, times(1)).writeDeveloperMock(Mockito.any(File.class), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void getCoreValidationSet() throws IOException {

        ValidationSetModel validationSetModel = Mockito.mock(ValidationSetModel.class);
        when(validationSetModel.getValidationSetName()).thenReturn("FOO");

        ValidationSetModel coreValidationSetModel = Mockito.mock(ValidationSetModel.class);
        when(coreValidationSetModel.getValidationSetName()).thenReturn(ExportConstants.CORE_VALIDATION_SET);

        List<ValidationSetModel> validations = new ArrayList<>();
        validations.add(validationSetModel);
        validations.add(coreValidationSetModel);

        ValidationSetModel actual = export.getCoreValidationSet(validations);
        assertThat(actual.getValidationSetName(), is(equalTo(ExportConstants.CORE_VALIDATION_SET)));
        assertThat(validations.size(), is(equalTo(1)));
        assertThat(validations.get(0).getValidationSetName(), is(equalTo("FOO")));
    }

    @DataProvider(name = "camelCaseValidationSetNameTestValues")
    public Object[][] camelCaseValidationSetNameTestValues() {
        return new Object[][] {
                { "", "" },
                { "first", "First" },
                { "a", "A" },
                { "AA", "AA" },
                { "aA", "AA" },
                { "andBecause", "AndBecause" },
                { "aa", "Aa" },
        };
    }

    @Test(dataProvider = "camelCaseValidationSetNameTestValues")
    public void camelCaseValidationSetName(String input, String expected) {
        String actual = export.camelCaseValidationSetName(input);
        assertThat(actual, is(equalTo(expected)));
    }
}