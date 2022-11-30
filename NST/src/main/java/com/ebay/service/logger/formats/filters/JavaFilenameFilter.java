package com.ebay.service.logger.formats.filters;

import java.io.File;
import java.io.FilenameFilter;

public class JavaFilenameFilter implements FilenameFilter {

  @Override
  public boolean accept(File dir, String name) {
    if (name.endsWith(".java")) {
      return true;
    }
    return false;
  }

}
