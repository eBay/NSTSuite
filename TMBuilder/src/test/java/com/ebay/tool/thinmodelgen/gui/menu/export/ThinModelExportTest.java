package com.ebay.tool.thinmodelgen.gui.menu.export;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.test.util.FileParser;
import com.ebay.test.util.ResourceParser;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.PathNode;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;

public class ThinModelExportTest {

  @Test
  public void exportThinModel() throws ClassNotFoundException, IOException {

    // Copy source file for editing by code generation, set it to delete on exit.
    String currentWorkingDirectory = System.getProperty("users.dir");
    String destinationFilePath = String.format("%s%s%s", currentWorkingDirectory, File.separator, "thinModelExportTest.java");

    String sourceFilePath = ResourceParser.getResourceFilePath("/com/ebay/tool/thinmodelgen/gui/menu/export/testOriginalFile.java");
    ResourceParser.copyFile(sourceFilePath, destinationFilePath);

    // Load the contents of the expected file for comparison against the generated one.
    String expectedFileContents = ResourceParser.readInResourceFile("/com/ebay/tool/thinmodelgen/gui/menu/export/test.java");

    // Generate the output
    PathNode[] pathNodes = new PathNode[4];
    pathNodes[0] = new PathNode("$", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
    pathNodes[1] = new PathNode("modules", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 1);
    pathNodes[2] = new PathNode("banners", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
    pathNodes[3] = new PathNode("_type", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType", 2);

    NodeModel nodeModel = new NodeModel(pathNodes,
        "rO0ABXNyADljb20uZWJheS50b29sLnRoaW5tb2RlbGdlbi5qc29uc2NoZW1hLnR5cGUuSnNvblN0cmluZ1R5cGU/+AfFIkAPoAIAAVsAB2VudW1TZXR0ABNbTGphdmEvbGFuZy9TdHJpbmc7eHIAN2NvbS5lYmF5LnRvb2wudGhpbm1vZGVsZ2VuLmpzb25zY2hlbWEudHlwZS5Kc29uQmFzZVR5cGW7QTqEJQ2EyAIABVoADnNraXBJbkpzb25QYXRoTAAQanNvblBhdGhOb2RlTmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO0wACG5vZGVUeXBlcQB+AANMABBwYXRoVG9KUENoZWNrTWFwdAATTGphdmEvdXRpbC9IYXNoTWFwO0wAEHByZXNlbnRhdGlvbk5hbWVxAH4AA3hwAHQABV90eXBldAAGU3RyaW5nc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAF0ABckLm1vZHVsZXMuYmFubmVycy5fdHlwZXNyACFjb20uZWJheS5qc29ucGF0aC5UTUpQU3RyaW5nQ2hlY2sAAAAAAAAAAQIAAHhyAB9jb20uZWJheS5qc29ucGF0aC5KUFN0cmluZ0NoZWNr6UCa4qd55TMCAAZMAA1jb250YWluc1ZhbHVlcQB+AANMABFlcXVhbHNPbmVPZlZhbHVlc3QAEExqYXZhL3V0aWwvTGlzdDtMAAtleGFjdExlbmd0aHQAE0xqYXZhL2xhbmcvSW50ZWdlcjtMAA1leHBlY3RlZFZhbHVlcQB+AANMABltYXhpbXVtTnVtYmVyT2ZDaGFyYWN0ZXJzcQB+AA5MABltaW5pbXVtTnVtYmVyT2ZDaGFyYWN0ZXJzcQB+AA54cHBwcHBwcHhxAH4ABnVyABNbTGphdmEubGFuZy5TdHJpbmc7rdJW5+kde0cCAAB4cAAAAAA=");

    NodeModel[] nodeModels = new NodeModel[1];
    nodeModels[0] = nodeModel;

    ValidationSetModel validationSet = new ValidationSetModel("coreValidation", nodeModels);

    ArrayList<ValidationSetModel> validationSetModels = new ArrayList<>();
    validationSetModels.add(validationSet);

    ThinModelExport thinModelExport = new ThinModelExport();
    File exportFile = new File(destinationFilePath);
    thinModelExport.export(exportFile, validationSetModels);

    // Read in the file contents and confirm it matches expected.
    String actualFileContents = FileParser.readInFile(exportFile);
    assertThat("Generated file contents must match expected.", actualFileContents, is(equalTo(expectedFileContents)));
  }

  @Test
  public void multipleExportsDoesNotIncreaseWhitespace() throws Exception {
	  
	    // Copy source file for editing by code generation, set it to delete on exit.
	    String currentWorkingDirectory = System.getProperty("users.dir");
	    String destinationFilePath = String.format("%s%s%s", currentWorkingDirectory, File.separator, "thinModelExportTest.java");

	    String sourceFilePath = ResourceParser.getResourceFilePath("/com/ebay/tool/thinmodelgen/gui/menu/export/testOriginalFile.java");
	    ResourceParser.copyFile(sourceFilePath, destinationFilePath);

	    // Load the contents of the expected file for comparison against the generated one.
	    String expectedFileContents = ResourceParser.readInResourceFile("/com/ebay/tool/thinmodelgen/gui/menu/export/test.java");

	    // Generate the output
	    PathNode[] pathNodes = new PathNode[4];
	    pathNodes[0] = new PathNode("$", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
	    pathNodes[1] = new PathNode("modules", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 1);
	    pathNodes[2] = new PathNode("banners", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonObjectType", 0);
	    pathNodes[3] = new PathNode("_type", "class com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType", 2);

	    NodeModel nodeModel = new NodeModel(pathNodes,
	        "rO0ABXNyADljb20uZWJheS50b29sLnRoaW5tb2RlbGdlbi5qc29uc2NoZW1hLnR5cGUuSnNvblN0cmluZ1R5cGU/+AfFIkAPoAIAAVsAB2VudW1TZXR0ABNbTGphdmEvbGFuZy9TdHJpbmc7eHIAN2NvbS5lYmF5LnRvb2wudGhpbm1vZGVsZ2VuLmpzb25zY2hlbWEudHlwZS5Kc29uQmFzZVR5cGW7QTqEJQ2EyAIABVoADnNraXBJbkpzb25QYXRoTAAQanNvblBhdGhOb2RlTmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO0wACG5vZGVUeXBlcQB+AANMABBwYXRoVG9KUENoZWNrTWFwdAATTGphdmEvdXRpbC9IYXNoTWFwO0wAEHByZXNlbnRhdGlvbk5hbWVxAH4AA3hwAHQABV90eXBldAAGU3RyaW5nc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAF0ABckLm1vZHVsZXMuYmFubmVycy5fdHlwZXNyACFjb20uZWJheS5qc29ucGF0aC5UTUpQU3RyaW5nQ2hlY2sAAAAAAAAAAQIAAHhyAB9jb20uZWJheS5qc29ucGF0aC5KUFN0cmluZ0NoZWNr6UCa4qd55TMCAAZMAA1jb250YWluc1ZhbHVlcQB+AANMABFlcXVhbHNPbmVPZlZhbHVlc3QAEExqYXZhL3V0aWwvTGlzdDtMAAtleGFjdExlbmd0aHQAE0xqYXZhL2xhbmcvSW50ZWdlcjtMAA1leHBlY3RlZFZhbHVlcQB+AANMABltYXhpbXVtTnVtYmVyT2ZDaGFyYWN0ZXJzcQB+AA5MABltaW5pbXVtTnVtYmVyT2ZDaGFyYWN0ZXJzcQB+AA54cHBwcHBwcHhxAH4ABnVyABNbTGphdmEubGFuZy5TdHJpbmc7rdJW5+kde0cCAAB4cAAAAAA=");

	    NodeModel[] nodeModels = new NodeModel[1];
	    nodeModels[0] = nodeModel;

	    ValidationSetModel validationSet = new ValidationSetModel("coreValidation", nodeModels);

	    ArrayList<ValidationSetModel> validationSetModels = new ArrayList<>();
	    validationSetModels.add(validationSet);

	    ThinModelExport thinModelExport = new ThinModelExport();
	    File exportFile = new File(destinationFilePath);
	    
	    // Simulate the writing of the file four times to confirm no extra lines are added.
	    thinModelExport.export(exportFile, validationSetModels);
	    thinModelExport.export(exportFile, validationSetModels);
	    thinModelExport.export(exportFile, validationSetModels);
	    thinModelExport.export(exportFile, validationSetModels);

	    // Read in the file contents and confirm it matches expected.
	    String actualFileContents = FileParser.readInFile(exportFile);
	    assertThat("Generated file contents must match expected.", actualFileContents, is(equalTo(expectedFileContents)));
  }

    @DataProvider(name = "lowerCaseCamelCaseValidationSetNameTestData")
    public Object[][] lowerCaseCamelCaseValidationSetNameTestData() {
        return new Object[][] {
                { "", "" },
                { "First", "first" },
                { "a", "a" },
                { "AA", "aA" },
                { "aA", "aA" },
                { "andBecause", "andBecause" },
                { "Aa", "aa" },
        };
    }

    @Test(dataProvider = "lowerCaseCamelCaseValidationSetNameTestData")
    public void lowerCaseCamelCaseValidationSetName(String input, String expected) {
        ThinModelExport export = new ThinModelExport();
        String actual = export.lowerCaseCamelCaseValidationSetName(input);
        assertThat(actual, is(equalTo(expected)));
    }
}
