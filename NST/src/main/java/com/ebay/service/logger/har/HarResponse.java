package com.ebay.service.logger.har;

import java.util.List;

import com.ebay.nst.coverage.Generated;

@Generated
public class HarResponse {

  private int status;
  private String statusText;
  private String httpVersion;
  private List<Header> headers;
  private Content content;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public void setHttpVersion(String httpVersion) {
    this.httpVersion = httpVersion;
  }

  public List<Header> getHeaders() {
    return headers;
  }

  public void setHeaders(List<Header> headers) {
    this.headers = headers;
  }

  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
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
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((headers == null) ? 0 : headers.hashCode());
    result = prime * result + ((httpVersion == null) ? 0 : httpVersion.hashCode());
    result = prime * result + status;
    result = prime * result + ((statusText == null) ? 0 : statusText.hashCode());
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
    if (!(obj instanceof HarResponse)) {
      return false;
    }
    HarResponse other = (HarResponse) obj;
    if (content == null) {
      if (other.content != null) {
        return false;
      }
    } else if (!content.equals(other.content)) {
      return false;
    }
    if (headers == null) {
      if (other.headers != null) {
        return false;
      }
    } else if (!headers.equals(other.headers)) {
      return false;
    }
    if (httpVersion == null) {
      if (other.httpVersion != null) {
        return false;
      }
    } else if (!httpVersion.equals(other.httpVersion)) {
      return false;
    }
    if (status != other.status) {
      return false;
    }
    if (statusText == null) {
      if (other.statusText != null) {
        return false;
      }
    } else if (!statusText.equals(other.statusText)) {
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
    return "HarResponse [status=" + status + ", statusText=" + statusText + ", httpVersion=" + httpVersion + ", headers=" + headers + ", content=" + content + "]";
  }

}
