package com.ebay.service.logger.writer;

/**
 * US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
 * ISO-8859-1    ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
 * UTF-8 Eight-bit UCS Transformation Format
 * UTF-16BE  Sixteen-bit UCS Transformation Format, big-endian byte order
 * UTF-16LE  Sixteen-bit UCS Transformation Format, little-endian byte order
 * UTF-16  Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
 */
public enum Encode {
  US_ASCII("US-ASCII"),
  ISO_8859_1("ISO-8859-1"),
  UTF_8("UTF-8"),
  UTF_16BE("UTF-16BE"),
  UTF_16LE("UTF-16LE"),
  UTF_16("UTF-16");

  private final String encoding;

  private Encode(String encoding) {
    this.encoding = encoding;
  }

  public String getValue() {
    return encoding;
  }
}
