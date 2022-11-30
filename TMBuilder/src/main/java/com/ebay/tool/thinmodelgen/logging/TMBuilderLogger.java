package com.ebay.tool.thinmodelgen.logging;

import com.ebay.tool.thinmodelgen.TMBuilderRuntimeArguments;

public class TMBuilderLogger {

  public static enum LOG_TYPE {
    INFO,
    DEGUG,
    WARNING,
    ERROR
  };

  public static void log(LOG_TYPE logType, String message) {

    if (!TMBuilderRuntimeArguments.getInstance().isVerboseLoggingEnabled()) {
      return;
    }

    System.out.println(String.format("%s: %s", logType, message));
  }
}
