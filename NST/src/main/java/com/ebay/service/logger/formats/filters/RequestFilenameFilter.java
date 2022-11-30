package com.ebay.service.logger.formats.filters;

import java.io.File;
import java.io.FilenameFilter;

public class RequestFilenameFilter implements FilenameFilter {

  @Override
  public boolean accept(File dir, String name) {
    if (name.contains("Request.json")) {
      return true;
    }
    return false;
  }
}
