package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.TMJPBooleanCheck;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class DeveloperMockExportTest {

    DeveloperMockExport export;

    @BeforeMethod(alwaysRun = true)
    public void resetDeveloperMockExport() {
        export = new DeveloperMockExport();
    }

    @Test
    public void testPopulateJsonModelWithBoolean() {
        TMJPBooleanCheck check = new TMJPBooleanCheck();
        check.setMockValue(true);
        export.populateJsonModel(check, "$.test.foo");
        Map<String, Object> jsonMap = export.getJson();
        JSONObject jsonObject = new JSONObject(jsonMap);
        MatcherAssert.assertThat(jsonObject.toString(), Matchers.is(Matchers.equalTo("{\"test\":{\"foo\":true}}")));
    }

    @Test
    public void testPopulateJsonModelWithBooleanWithOverride() {
        TMJPBooleanCheck check = new TMJPBooleanCheck();
        check.setMockValue(true);
        export.populateJsonModel(check, "$.test.foo");

        check.setMockValue(false);
        export.populateJsonModel(check, "$.test.foo");

        Map<String, Object> jsonMap = export.getJson();
        JSONObject jsonObject = new JSONObject(jsonMap);
        MatcherAssert.assertThat(jsonObject.toString(), Matchers.is(Matchers.equalTo("{\"test\":{\"foo\":false}}")));
    }
}