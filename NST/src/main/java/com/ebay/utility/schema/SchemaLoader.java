package com.ebay.utility.schema;

import com.ebay.utility.unzip.UnzipUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SchemaLoader {

	private static Map<String, JsonNode> jsonNodeCache = new HashMap<>();

	/**
	 * Get JsonNode object from the specified schema file resource path. Can be JSON or ZIP.
	 *
	 * @param resourceFilePath Path to the schema file resource.
	 * @return Parsed schema root node.
	 * @throws IOException Pass IOExceptions up the call stack.
	 */
	public synchronized static JsonNode loadSchema(String resourceFilePath) throws IOException {
		JsonNode node;
		String fileName = resourceFilePath.substring(resourceFilePath.lastIndexOf("/") + 1);

		if (jsonNodeCache.containsKey(fileName)) {
			return jsonNodeCache.get(fileName);
		}

		if (resourceFilePath.endsWith(".zip")) {
			InputStream zipFileInputStream = SchemaLoader.class.getResourceAsStream(resourceFilePath);
			List<String> unzippedFilePaths = UnzipUtil.unzipFile(zipFileInputStream);
			zipFileInputStream.close();
			node = getNodeFromUnzippedFilePaths(unzippedFilePaths);
		} else {
			node = JsonLoader.fromResource(resourceFilePath);
		}

		jsonNodeCache.put(fileName, node);
		return node;
	}

	/**
	 * Get JsonNode object from the specified schema file. Can be JSON or ZIP.
	 *
	 * @param file The schema file.
	 * @return Parsed schema root node.
	 * @throws IOException Pass IOExceptions up the call stack.
	 */
	public synchronized static JsonNode loadSchema(File file) throws IOException {
		Objects.requireNonNull(file, "File MUST NOT be null when calling loadSchema().");
		String fileName = file.getName();
		JsonNode node;

		if (jsonNodeCache.containsKey(fileName)) {
			return jsonNodeCache.get(fileName);
		}

		if (FilenameUtils.isExtension(file.getName(), "zip")) {
			List<String> unzippedFilePaths = UnzipUtil.unzipFile(file.getAbsolutePath());
			node = getNodeFromUnzippedFilePaths(unzippedFilePaths);
		} else {
			node = JsonLoader.fromFile(file);
		}

		jsonNodeCache.put(fileName, node);
		return node;
	}

	/**
	 * Clears the JSON node cache.
	 */
	public static void clearJsonNodeCache() {
		jsonNodeCache.clear();
	}

	/**
	 * Use the specified list of paths to create a JsonNode object.
	 * @param paths List of paths, expecting 1.
	 * @return JsonNode object created from unzipped file path.
	 * @throws IOException Exception thrown when loading the specified path.
	 */
	private synchronized static JsonNode getNodeFromUnzippedFilePaths(List<String> paths) throws IOException {
		if (paths.isEmpty()) {
			throw new IllegalArgumentException("Provided zip file did not contain any files. Please specify zip files containing a schema file.");
		} else if (paths.size() > 1) {
			throw new IllegalArgumentException("Provided zip file contains more than 1 file. Unable to determine which schema to use. Please specify zip files with a single schema.");
		}

		JsonNode node = JsonLoader.fromPath(paths.get(0));
		UnzipUtil.cleanupExtractedFiles(paths);
		return node;
	}

}
