package com.ebay.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ResourceParser {

  /**
   * Read the contents of a resource file and return that contents as a string.
   *
   * @param resourceFile
   *          Resource file to read.
   * @return Contents of resource file.
   * @throws IOException
   *           IO Error.
   */
  public static String readInResourceFile(String resourceFile) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceParser.class.getResourceAsStream(resourceFile)));
    StringBuilder fileContents = new StringBuilder();
    try {

      String line;
      while ((line = reader.readLine()) != null) {
        fileContents.append(line);
        fileContents.append("\n");
      }

    } finally {
      reader.close();
    }

    String text = fileContents.toString();
    text = text.substring(0, text.lastIndexOf("\n"));
    return text;
  }

  public static JsonNode readInResourceJson(String resourceFile) throws IOException {
    return new ObjectMapper().readTree(readInResourceFile(resourceFile));
  }
  /**
   * Get the full path to the resource file.
   *
   * @param resourceFile
   *          Resource file to locate on the local file system.
   * @return Full resource file path.
   */
  public static String getResourceFilePath(String resourceFile) {

    URL resource = ResourceParser.class.getResource(resourceFile);
    String path = resource.getPath();
    return path;
  }
}
