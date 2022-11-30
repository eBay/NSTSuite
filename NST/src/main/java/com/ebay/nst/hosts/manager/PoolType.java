package com.ebay.nst.hosts.manager;

public enum PoolType {
  PROD,
  QA,
  FEATURE,
  DEV;

  public static PoolType getType(String poolName) {

    for (PoolType type : PoolType.values()) {
      if (type.name().equalsIgnoreCase(poolName)) {
        return type;
      }
    }

    throw new IllegalArgumentException(String.format("Unable to match %s to a PoolType.", poolName));
  }
}
