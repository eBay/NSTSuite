package com.ebay.service.logger.har;

import com.ebay.nst.coverage.Generated;

@Generated
public class Entry {

  private HarRequest request;
  private HarResponse response;

  public HarRequest getRequest() {
    return request;
  }

  public void setRequest(HarRequest request) {
    this.request = request;
  }

  public HarResponse getResponse() {
    return response;
  }

  public void setResponse(HarResponse response) {
    this.response = response;
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
    result = prime * result + ((request == null) ? 0 : request.hashCode());
    result = prime * result + ((response == null) ? 0 : response.hashCode());
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
    if (!(obj instanceof Entry)) {
      return false;
    }
    Entry other = (Entry) obj;
    if (request == null) {
      if (other.request != null) {
        return false;
      }
    } else if (!request.equals(other.request)) {
      return false;
    }
    if (response == null) {
      if (other.response != null) {
        return false;
      }
    } else if (!response.equals(other.response)) {
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
    return "Entry [request=" + request + ", response=" + response + "]";
  }

}
