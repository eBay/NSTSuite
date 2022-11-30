package com.ebay.utility.timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

  /**
   * Get the current time stamp with the format: `2021/02/24 16:32:20`.
   *
   * @return Time stamp string.
   */
  public static String getCurrentTime() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }
}
