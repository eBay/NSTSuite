package com.ebay.service.logger.har;

import java.util.List;

import com.ebay.nst.coverage.Generated;

@Generated
public class Log {

  private String version;
  private Creator creator;
  private List<Entry> entries;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Creator getCreator() {
    return creator;
  }

  public void setCreator(Creator creator) {
    this.creator = creator;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creator == null) ? 0 : creator.hashCode());
    result = prime * result + ((entries == null) ? 0 : entries.hashCode());
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Log)) {
      return false;
    }
    Log other = (Log) obj;
    if (creator == null) {
      if (other.creator != null) {
        return false;
      }
    } else if (!creator.equals(other.creator)) {
      return false;
    }
    if (entries == null) {
      if (other.entries != null) {
        return false;
      }
    } else if (!entries.equals(other.entries)) {
      return false;
    }
    if (version == null) {
      if (other.version != null) {
        return false;
      }
    } else if (!version.equals(other.version)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Log [version=" + version + ", creator=" + creator + ", entries=" + entries + "]";
  }

}
