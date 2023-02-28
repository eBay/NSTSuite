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

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeveloperMockExportTest {

    DeveloperMockExport export;

    @BeforeMethod(alwaysRun = true)
    public void resetDeveloperMockExport() {
        export = new DeveloperMockExport();
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