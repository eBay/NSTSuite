package com.ebay.service.logger.formats.filters;

import java.io.File;
import java.io.FilenameFilter;

public class AndroidFilenameFilter implements FilenameFilter {

  @Override
  public boolean accept(File dir, String name) {
    if (name.endsWith(".java") || name.endsWith(".kt")) {
      return true;
    }
    return false;
  }

}
