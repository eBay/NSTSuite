package com.ebay.service.logger.platforms.util;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.service.logger.formats.filters.JavaFilenameFilter;
import com.ebay.service.logger.formats.filters.KotlinFilenameFilter;
import com.ebay.service.logger.formats.filters.SwiftFilenameFilter;
import org.testng.Reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PlatformTestFileManager {

  private File platformTestDirectory;
  private ArrayList<File> filesInPlatformTestDirectory = new ArrayList<>();
  boolean platformOutputDisabled = false;

  /**
   * Create a new instance with the specified path.
   * @throws FileNotFoundException If the path does not exist or is not a directory.
   */
  public PlatformTestFileManager() throws FileNotFoundException {

    File[] files;
    String path;

    switch (RuntimeConfigManager.getInstance().getPlatform()) {
    case IOS:
      path = RuntimeConfigManager.getInstance().getIosTestsLocation();
      if (path == null) {
    	Reporter.log("iOS test class path is undefined. Skipping update of iOS test classes.", true);
        platformOutputDisabled = true;
        return;
      }
      checkDirectory(path);
      files = platformTestDirectory.listFiles(new SwiftFilenameFilter());
      for (File file : files) {
        filesInPlatformTestDirectory.add(file);
      }
      break;
    case ANDROID:
      path = RuntimeConfigManager.getInstance().getAndroidTestsLocation();
      if (path == null) {
    	Reporter.log("Android test class path is undefined. Skipping update of Android test classes.", true);
        platformOutputDisabled = true;
        return;
      }
      checkDirectory(path);
      files = platformTestDirectory.listFiles(new JavaFilenameFilter());
      for (File file : files) {
        filesInPlatformTestDirectory.add(file);
      }
      break;
    case ANDROID_KOTLIN:
      path = RuntimeConfigManager.getInstance().getAndroidTestsLocation();
      if (path == null) {
        Reporter.log("Android test class path is undefined. Skipping update of Android test classes.", true);
        platformOutputDisabled = true;
        return;
      }
      checkDirectory(path);
      files = platformTestDirectory.listFiles(new KotlinFilenameFilter());
      for (File file : files) {
        filesInPlatformTestDirectory.add(file);
      }
      break;
    case MWEB:
    case SITE:
    default:
      break;
    }
  }

  /**
   * Check if platform specific FUI output is disabled.
   * @return True if disabled, false otherwise.
   */
  public boolean isPlatformOutputDisabled() {
    return platformOutputDisabled;
  }

  /**
   * Check if the directory has a test file with the specified name.
   * @param withFilename File name to match.
   * @return True if directory has the requested file, false otherwise.
   */
  public boolean hasTestFile(String withFilename) {
    withFilename = appendAppropriateSuffix(withFilename);
    for (File file : filesInPlatformTestDirectory) {
      if (file.getName().equals(withFilename)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the platform test file with the specified name.
   * @param withFilename Filename to retrieve from FUI file path.
   * @return File instance, or null if not found.
   */
  public File getTestFile(String withFilename) {
    withFilename = appendAppropriateSuffix(withFilename);
    for (File file : filesInPlatformTestDirectory) {
      if (file.getName().equals(withFilename)) {
        return file;
      }
    }
    return null;
  }

  /**
   * Check that the directory specified by the runtime configuration exists.
   * @param path
   * @throws FileNotFoundException
   */
  private void checkDirectory(String path) throws FileNotFoundException {
    platformTestDirectory = new File(path);
    if (!platformTestDirectory.exists()) {
      throw new FileNotFoundException(String.format("Path: %s, does not exist.", path));
    } else if (!platformTestDirectory.isDirectory()) {
      throw new FileNotFoundException(String.format("Path: %s, is not a directory.", path));
    }
  }

  private String appendAppropriateSuffix(String filename) {

    if (!filename.contains(".")) {
      switch (RuntimeConfigManager.getInstance().getPlatform()) {
      case IOS:
        return filename + ".swift";
      case ANDROID:
        return filename + ".java";
      case ANDROID_KOTLIN:
        return filename + ".kt";
      case MWEB:
      case SITE:
      default:
        break;
      }
    }

    return filename;
  }
}
