package com.ebay.service.logger.platforms.util;

import com.ebay.runtime.RuntimeConfigManager;
import com.ebay.runtime.arguments.Platform;

public class PlatformConstants {

  public static String getPlatformSpecificTab() {

    Platform platform = RuntimeConfigManager.getInstance().getPlatform();
    String tabCharacter = "\t";
    switch (platform) {
    case ANDROID:
      tabCharacter = "\t";
      break;
    case IOS:
      tabCharacter = "" + '\u0009';
      break;
    case SITE:
    case MWEB:
      break;
    }
    return tabCharacter;
  }
}
