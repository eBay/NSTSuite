package com.ebay.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileParser {

  /**
   * Read in the file contents.
   *
   * @param file
   *          File to read.
   * @return File contents.
   * @throws IOException
   *           Pass through error.
   */
  public static String readInFile(File file) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
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
}
