package com.ebay.service.logger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.ebay.service.logger.writer.Encode;

public class FileReaderWithEncoding {

  private BufferedReader reader;

  public FileReaderWithEncoding(File file, Encode encode) throws UnsupportedEncodingException, FileNotFoundException {
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encode.getValue());
    reader = new BufferedReader(inputStreamReader);
  }

  public FileReaderWithEncoding(String filePath, Encode encode) throws UnsupportedEncodingException, FileNotFoundException {
    this (new File(filePath), encode);
  }

  /**
   * Reads a line of text. A line is considered to be terminated by any one of a line feed ('\n'), a carriage return ('\r'), or a carriage return followed immediately by a linefeed.
   * @return A String containing the contents of the line, not including any line-termination characters, or null if the end of the stream has been reached
   * @throws IOException If an I/O error occurs
   */
  public String readLine() throws IOException {
    return reader.readLine();
  }

  /**
   * Closes the stream and releases any system resources associated with it. Once the stream has been closed, further read(), ready(), mark(), reset(), or skip() invocations will throw an IOException. Closing a previously closed stream has no effect.
   * @throws IOException If an I/O error occurs
   */
  public void close() throws IOException {
    reader.close();
  }
}
