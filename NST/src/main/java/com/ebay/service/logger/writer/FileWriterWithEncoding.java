package com.ebay.service.logger.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * This is a wrapper around a OutputStreamWriter that cuts out the multiple
 * lines needed to write to file with a specific encoding. All of the write
 * operations from abstract base class Writer.java are wrapped. All operations
 * are re-write operations on the file and do NOT append.
 */
public class FileWriterWithEncoding {

  private BufferedWriter writer;

  public FileWriterWithEncoding(File file, Encode encode) throws UnsupportedEncodingException, FileNotFoundException {
	
	if (!file.exists()) {
      file.getParentFile().mkdirs();
    }
	
	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, false), encode.getValue());
    writer = new BufferedWriter(outputStreamWriter);
  }

  public FileWriterWithEncoding(String filePath, Encode encode) throws UnsupportedEncodingException, FileNotFoundException {
    this(new File(filePath), encode);
  }

  public void write(int c) throws IOException {
    writer.write(c);
  }

  public void write(char[] buffer) throws IOException {
    writer.write(buffer);
  }

  public void write(char[] buffer, int offset, int length) throws IOException {
    writer.write(buffer, offset, length);
  }

  public void write(String data) throws IOException {
    writer.write(data);
  }

  public void write(String data, int offset, int length) throws IOException {
    writer.write(data, offset, length);
  }

  public void flush() throws IOException {
    writer.flush();
  }

  public void close() throws IOException {
    writer.close();
  }
}
