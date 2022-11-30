package com.ebay.service.logger.formats.filters;

import java.io.File;
import java.io.FilenameFilter;

public class HarFilenameFilter implements FilenameFilter {

  @Override
  public boolean accept(File dir, String name) {

    if (name.endsWith(".har")) {
      return true;
    }
    return false;
  }

}
