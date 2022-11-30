package com.ebay.tool.thinmodelgen.gui.menu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.tool.thinmodelgen.gui.menu.filemodel.FileOperationHandler;

public class TMBuilderMenuTest {
	
	FileOperationHandler handler = mock(FileOperationHandler.class);
	TMBuilderMenu menu = new TMBuilderMenu(handler);
	
	@DataProvider(name = "convertPathToRelativePathValues")
	public Object[][] convertPathToRelativePathValues() {
		
		String cwd = System.getProperty("user.dir");
		
		return new Object[][] {
			{ "/Users/id/dev/project/folder", "/Users/id/dev/project/folder/src/main/com/ebay/foo", "src/main/com/ebay/foo" },
			{ "/Users/id/dev/project/folder", "/Users/id/dev/project/folder", "" },
			{ "/Users/id/dev/project/folder", "/Users/id/dev/project/folder/src/main/com/ebay/foo.txt", "src/main/com/ebay/foo.txt" },
			{ cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.tmb", cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.yaml", "api-complex-composition.yaml" },
			{ cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.tmb", cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/jsonschema/parser/jsonschema/invalidArraySchema.json", "../jsonschema/parser/jsonschema/invalidArraySchema.json" },
		};
	}

	@Test(dataProvider = "convertPathToRelativePathValues")
	public void convertPathToRelativePath(String tmbFilePath, String otherFilePath, String expectedPath) {
		
		String relativePath = menu.convertPathToRelativePath(tmbFilePath, otherFilePath);
		assertThat(relativePath, is(equalTo(expectedPath)));
	}
	
	@DataProvider(name = "relativePathTestValues")
	public Object[][] relativePathTestValues() {
		
		String cwd = System.getProperty("user.dir");
		
		return new Object[][] {
			{ cwd, "src/test", cwd + "/src/test" },
			{ cwd, "../tmbuilder/src/test", cwd + "/../tmbuilder/src/test" },
			{ "/Users/id/dev/project/folder", "../test/path", "../test/path" }, // Fall back case for existing TMB files.
			{ cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.tmb", "api-complex-composition.yaml", cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.yaml" },
			{ cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/api-complex-composition.tmb", "../jsonschema/parser/jsonschema/invalidArraySchema.json", cwd + "/src/test/resources/com/ebay/tool/thinmodelgen/testopenapi/../jsonschema/parser/jsonschema/invalidArraySchema.json" },
		};
	}
	
	@Test(dataProvider = "relativePathTestValues")
	public void resolveRelativePath(String tmbPath, String relativePath, String expectedPath) {
		
		String resolvedPath = menu.resolveRelativePath(tmbPath, relativePath);
		assertThat(resolvedPath, is(equalTo(expectedPath)));
	}
}
