package com.ebay.service.logger.platforms.blocksignatures;

public enum BlockSignatureType {
  MEMBER_FIELD("MEMBER FIELD"),
  METHOD("METHOD"),
  INJECTION("MOCK INJECTION"),
  UNKNOWN("UNKNOWN");

  private final String value;

  private BlockSignatureType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
