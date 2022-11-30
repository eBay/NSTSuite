package com.ebay.utility.unzip;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class UnzipUtilTest {

  String testFile1 = "test1.txt";
  String testFile2 = "test2.txt";

  //
  // Unzip tests
  //
  @Test(enabled = true, groups = "unitTest")
  public void testUnzipMultipleFilesUsingFilePath() throws IOException {
    String filePath = getClass().getClassLoader().getResource("com/ebay/utility/unzip/multipleFileTest.zip").getFile();
    List<String> unzippedFiles = UnzipUtil.unzipFile(filePath);

    String parentPath = getParentPath(unzippedFiles);

    List<String> expectedFiles = Arrays.asList(parentPath + testFile1, parentPath + testFile2);
    assertThat("We should have the expected list of files.", unzippedFiles, is(equalTo(expectedFiles)));
  }

  @Test(enabled = true, groups = "unitTest")
  public void testUnzipSingleFileUsingFilePath() throws IOException {
    String filePath = getClass().getClassLoader().getResource("com/ebay/utility/unzip/singleFileTest.zip").getFile();
    List<String> unzippedFiles = UnzipUtil.unzipFile(filePath);

    String parentPath = getParentPath(unzippedFiles);

    List<String> expectedFiles = Arrays.asList(parentPath + testFile1);
    assertThat("We should have the expected file.", unzippedFiles, is(equalTo(expectedFiles)));
  }

  @Test(enabled = true, groups = "unitTest")
  public void testUnzipMultipleFilesUsingStream() throws IOException {
    InputStream testMultipleFile = getClass().getClassLoader().getResourceAsStream("com/ebay/utility/unzip/multipleFileTest.zip");
    List<String> unzippedFiles = UnzipUtil.unzipFile(testMultipleFile);

    String parentPath = getParentPath(unzippedFiles);

    List<String> expectedFiles = Arrays.asList(parentPath + testFile1, parentPath + testFile2);
    assertThat("We should have the expected list of files.", unzippedFiles, is(equalTo(expectedFiles)));
  }

  @Test(enabled = true, groups = "unitTest")
  public void testUnzipSingleFileUsingStream() throws IOException {
    InputStream testSingleFile = getClass().getClassLoader().getResourceAsStream("com/ebay/utility/unzip/singleFileTest.zip");
    List<String> unzippedFiles = UnzipUtil.unzipFile(testSingleFile);

    String parentPath = getParentPath(unzippedFiles);

    List<String> expectedFiles = Arrays.asList(parentPath + testFile1);
    assertThat("We should have the expected file.", unzippedFiles, is(equalTo(expectedFiles)));
  }

  @Test(enabled = true, expectedExceptions = IllegalArgumentException.class, groups = "unitTest")
  public void testUnzipWithInvalidPathUsingStream() throws IOException {
    InputStream testInvalidPathFile = getClass().getClassLoader().getResourceAsStream("com/ebay/utility/unzip/notFound.zip");
    UnzipUtil.unzipFile(testInvalidPathFile);
  }

  @Test(enabled = true, expectedExceptions = IllegalArgumentException.class, groups = "unitTest")
  public void testUnzipWithNullInputStream() throws IOException {
    UnzipUtil.unzipFile((InputStream) null);
  }

  @Test(enabled = true, expectedExceptions = IllegalArgumentException.class, groups = "unitTest")
  public void testUnzipWithNullFilePath() throws IOException {
    UnzipUtil.unzipFile((String) null);
  }

  //
  // Cleanup tests
  //

  @Test
  public void testCleanupWithNullList() {
    UnzipUtil.cleanupExtractedFiles(null); // No exception should throw
  }

  @Test
  public void testCleanupWithEmptyList() {
    UnzipUtil.cleanupExtractedFiles(new ArrayList<String>());
  }

  @Test
  public void testCleanupFiles() throws IOException {
    InputStream testMultipleFile = getClass().getClassLoader().getResourceAsStream(
        "com/ebay/utility/unzip/multipleFileTest.zip");

    List<String> unzippedFiles = UnzipUtil.unzipFile(testMultipleFile);
    String parentPath = getParentPath(unzippedFiles);
    File parentFolder = new File(parentPath);
    String[] startingFolderContents = parentFolder.list();

    UnzipUtil.cleanupExtractedFiles(unzippedFiles);
    String[] cleanedUpFolderContents = parentFolder.list();

    assertThat("There must be two fewer files than the directory initially had after cleaning up.", startingFolderContents.length - cleanedUpFolderContents.length, is(equalTo(2)));

    ArrayList<String> resultingList = new ArrayList<>(Arrays.asList(startingFolderContents));
    ArrayList<String> filesThatAreLeftAfterCleanup = new ArrayList<>(Arrays.asList(cleanedUpFolderContents));
    resultingList.removeAll(filesThatAreLeftAfterCleanup);

    List<String> expectedFiles = Arrays.asList(testFile1, testFile2);
    assertThat("Files that were removed do not match expected.", resultingList, is(equalTo(expectedFiles)));
  }

  //
  // Private methods
  //

  private String getParentPath(List<String> unzippedFilePaths) {
    if (unzippedFilePaths.isEmpty()) {
      throw new IllegalArgumentException("unzippedFilePaths was empty. Must have at least 1 path.");
    }

    File file = new File(unzippedFilePaths.get(0));
    return file.getParent() + "/";
  }
}
