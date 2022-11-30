package com.ebay.service.logger.har;

import com.ebay.nst.coverage.Generated;

@Generated
public class Har {

  private Log log;

  public Log getLog() {
    return log;
  }

  public void setLog(Log log) {
    this.log = log;
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
    result = prime * result + ((log == null) ? 0 : log.hashCode());
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
    if (!(obj instanceof Har)) {
      return false;
    }
    Har other = (Har) obj;
    if (log == null) {
      if (other.log != null) {
        return false;
      }
    } else if (!log.equals(other.log)) {
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
    return "Har [log=" + log + "]";
  }

}
