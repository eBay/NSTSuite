package com.ebay.test.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class ResourceParser {

  private static String currentWorkingDirectory = System.getProperty("user.dir");

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

  /**
   * Get the local file path for the specified library and library file.
   *
   * @param library
   *          Library that was copied over from project resources.
   * @param libraryFile
   *          Library file, from the specified library, that was copied over
   *          from project resources.
   * @return Path to local file.
   */
  public static String getLocalFilePath(String library, String libraryFile) {
    return String.format("%s%slibs%s%s%s%s", currentWorkingDirectory, File.separator, File.separator, library, File.separator, libraryFile);
  }

  /**
   * Copy the specified source file to the destination file. The destination
   * file will be deleted on exit.
   *
   * @param sourceFilePath
   *          Source file path.
   * @param destinationFilePath
   *          Destination file path.
   * @throws IOException
   */
  public static void copyFile(String sourceFilePath, String destinationFilePath) {

    requireNonNull(sourceFilePath, "Source file path must be non null.");
    requireNonNull(destinationFilePath, "Destination file path must be non null.");

    File sourceFile = new File(sourceFilePath);
    File destinationFile = new File(destinationFilePath);
    destinationFile.deleteOnExit();

    if (!sourceFile.exists()) {
      throw new IllegalArgumentException(String.format("Source file [%s] does not exist. Unable to copy.", sourceFilePath));
    }

    try {
      FileUtils.copyFile(sourceFile, destinationFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
