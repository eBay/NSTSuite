package com.ebay.service.logger.platforms.util;

import java.util.HashMap;

public class PlatformApiCallCounter {

  private static PlatformApiCallCounter instance;
  private HashMap<String, Integer> apiCounterMap = new HashMap<>();

  private PlatformApiCallCounter() {

  }

  /**
   * Get the instance.
   * @return Singleton instance.
   */
  public static PlatformApiCallCounter getInstance() {

    if (instance == null) {
      synchronized (PlatformApiCallCounter.class) {
        if (instance == null) {
          instance = new PlatformApiCallCounter();
        }
      }
    }

    return instance;
  }

  /**
   * Clear all counters.
   */
  public void clear() {
    apiCounterMap.clear();
  }

  /**
   * Get the API counter value for the specified API name.
   * @param apiName API name to increment counter for.
   * @return The current counter value for the specified API, or 0 if not currently tracked.
   */
  public int getApiCountFor(String apiName) {
    Integer count = apiCounterMap.get(apiName);
    if (count == null) {
      return 0;
    }
    return count;
  }

  /**
   * Increment the counter for the specified API name.
   * @param apiName API name to increment counter for.
   * @return The newly incremented counter value;
   */
  public int incrementApiCountFor(String apiName) {
    Integer count = apiCounterMap.get(apiName);
    if (count == null) {
      count = 1;
    } else {
      count++;
    }
    apiCounterMap.put(apiName, count);
    return count;
  }
}
