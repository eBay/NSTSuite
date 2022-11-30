package com.ebay.service.logger.formats.filters;

import java.io.File;
import java.io.FilenameFilter;

public class ResponseFilenameFilter implements FilenameFilter {

  @Override
  public boolean accept(File dir, String name) {
    if (name.contains("Response.json")) {
      return true;
    }
    return false;
  }

}
