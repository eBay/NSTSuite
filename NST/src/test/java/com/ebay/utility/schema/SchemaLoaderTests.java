package com.ebay.utility.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class SchemaLoaderTests {

	@Test
	public void testLoadSchemaFromCache() throws IOException {

		File originalSchema = getFileFromResource("/schema/schemaLoaderSingle.zip");
		File tempSchemaCopy = File.createTempFile("schemaLoaderCopy", ".zip");
		FileUtils.copyFile(originalSchema, tempSchemaCopy);

		// Load to cache first
		SchemaLoader.loadSchema(tempSchemaCopy);

		// Create empty file that has the same name as tempSchemaCopy
		File second = new File("/" + tempSchemaCopy.getName());
		second.deleteOnExit();
		tempSchemaCopy.delete();
		JsonNode jsonNodeFromCache = SchemaLoader.loadSchema(second);

		Assert.assertNotNull(jsonNodeFromCache);
	}

	@Test
	public void testLoadZippedSchemaFromResourceFilePath() throws IOException {
		JsonNode jsonNode = SchemaLoader.loadSchema("/schema/schemaLoaderSingle.zip");
		Assert.assertNotNull(jsonNode);
	}

	@Test
	public void testLoadUnzippedSchemaFromResourceFilePath() throws IOException {
		JsonNode jsonNode = SchemaLoader.loadSchema("/schema/schemaLoader.json");
		Assert.assertNotNull(jsonNode);
	}

	@Test
	public void testLoadZippedSchemaFromFile() throws IOException, URISyntaxException {
		JsonNode jsonNode = SchemaLoader.loadSchema(getFileFromResource("/schema/schemaLoaderSingle.zip"));
		Assert.assertNotNull(jsonNode);
	}

	@Test
	public void testLoadUnzippedSchemaFromFile() throws IOException {
		JsonNode jsonNode = SchemaLoader.loadSchema(getFileFromResource("/schema/schemaLoader.json"));
		Assert.assertNotNull(jsonNode);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, groups = "unitTest")
	public void testLoadSchemaFromMultipleZippedFile() throws IOException {
		SchemaLoader.loadSchema(getFileFromResource("/schema/schemaLoaderMultiple.zip"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class, groups = "unitTest")
	public void testLoadSchemaFromEmptyZippedFile() throws IOException {
		SchemaLoader.loadSchema(getFileFromResource("/schema/schemaLoaderEmpty.zip"));
	}

	private File getFileFromResource(String path) {
		URL url = SchemaLoaderTests.class.getResource(path);
		return FileUtils.toFile(url);
	}
}
