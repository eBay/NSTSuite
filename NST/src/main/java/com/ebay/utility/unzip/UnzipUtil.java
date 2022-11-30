package com.ebay.utility.unzip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.testng.Reporter;

public class UnzipUtil {

  /**
   * Unzips the specified file and returns list containing file paths of contents.
   *
   * @param zipFilePath
   *          Path to zip file to unzip.
   * @return List of file paths that were unzipped from the zip.
   * @throws IOException
   *           File operation exceptions.
   */
  public static List<String> unzipFile(String zipFilePath) throws IOException {

    if (zipFilePath == null) {
      throw new IllegalArgumentException("Zip file path cannot be null.");
    }

    File zipFile = new File(zipFilePath);
    return getUnzippedFilePathsFromZipFile(zipFile);
  }

  /**
   * Unzips the file copied from the specified InputStream and returns list containing file paths of contents.
   *
   * @param resourceFileInputStream
   *          inputStream pointing to resource containing zip file to unzip.
   * @return List of file paths that were unzipped from the zip.
   * @throws IOException
   *           File operation exceptions.
   */
  public static List<String> unzipFile(InputStream resourceFileInputStream) throws IOException {

    if (resourceFileInputStream == null) {
      throw new IllegalArgumentException("inputStream cannot be null.");
    }

    File tmpZipFile = File.createTempFile("file", "temp");
    FileUtils.copyInputStreamToFile(resourceFileInputStream, tmpZipFile);

    List<String> unzippedPaths = getUnzippedFilePathsFromZipFile(tmpZipFile);
    tmpZipFile.deleteOnExit();
    return unzippedPaths;
  }

  /**
   * Delete each of the files specified in the list. Use this to remove the
   * extracted files returned from unzipFile().
   *
   * @param extractedFiles
   *          List of files to remove.
   */
  public static void cleanupExtractedFiles(List<String> extractedFiles) {

    if (extractedFiles == null) {
      return;
    }

    for (String file : extractedFiles) {
      new File(file).delete();
    }
  }

  /**
   * Uses the passed zip file, unzips it, and returns a list of content file paths.
   *
   * @param zipFile Zip file to unzip.
   * @return List of file paths of unzipped contents.
   * @throws IOException
   */
  private static List<String> getUnzippedFilePathsFromZipFile(File zipFile) throws IOException {
    String path = zipFile.getParent();

    byte[] buffer = new byte[1024];
    FileInputStream fileInputStream = new FileInputStream(zipFile);
    ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
    ZipEntry zipEntry = zipInputStream.getNextEntry();
    List<String> unzippedFilePaths = new ArrayList<>();

    while (zipEntry != null) {
      String fileName = zipEntry.getName();

      Reporter.log(String.format("Unzipping [%s] from [%s]", fileName, zipFile.getAbsolutePath()), true);

      File newFile = new File(path + File.separator + fileName);
      unzippedFilePaths.add(newFile.getAbsolutePath());

      FileOutputStream fileOutputStream = new FileOutputStream(newFile);

      int len;
      while ((len = zipInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
      }

      fileOutputStream.close();
      zipInputStream.closeEntry();
      zipEntry = zipInputStream.getNextEntry();
    }

    fileInputStream.close();
    zipInputStream.closeEntry();
    zipInputStream.close();

    return unzippedFilePaths;
  }
}
