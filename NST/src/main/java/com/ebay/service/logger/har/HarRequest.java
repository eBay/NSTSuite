package com.ebay.service.logger.har;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ebay.nst.NstRequestType;
import com.ebay.nst.coverage.Generated;

@Generated
public class HarRequest {

  private NstRequestType method;
  private String url;
  private String httpVersion;
  private List<Header> headers;
  private List<QueryString> queryString;
  private PostData postData;

  public NstRequestType getMethod() {
    return method;
  }

  public void setMethod(NstRequestType method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUrl(URL url) {
    this.url = url.toString();
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

  /**
   * If an Authorization header is included, the value will be obfuscated for security reasons.
   * @param headers Headers to set.
   */
  public void setHeaders(List<Header> headers) {
    this.headers = new ArrayList<>(headers);
    for (Header header : this.headers) {
      if (header.getName().equalsIgnoreCase("Authorization")) {
        header.setValue("Bearer v^obfuscated123");
      }
    }
  }

  public List<QueryString> getQueryString() {
    return queryString;
  }

  public void setQueryString(List<QueryString> queryString) {
    this.queryString = queryString;
  }

  public PostData getPostData() {
    return postData;
  }

  public void setPostData(PostData postData) {
    this.postData = postData;
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
    result = prime * result + ((headers == null) ? 0 : headers.hashCode());
    result = prime * result + ((httpVersion == null) ? 0 : httpVersion.hashCode());
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    result = prime * result + ((postData == null) ? 0 : postData.hashCode());
    result = prime * result + ((queryString == null) ? 0 : queryString.hashCode());
    result = prime * result + ((url == null) ? 0 : url.hashCode());
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
    if (!(obj instanceof HarRequest)) {
      return false;
    }
    HarRequest other = (HarRequest) obj;
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
    if (method != other.method) {
      return false;
    }
    if (postData == null) {
      if (other.postData != null) {
        return false;
      }
    } else if (!postData.equals(other.postData)) {
      return false;
    }
    if (queryString == null) {
      if (other.queryString != null) {
        return false;
      }
    } else if (!queryString.equals(other.queryString)) {
      return false;
    }
    if (url == null) {
      if (other.url != null) {
        return false;
      }
    } else if (!url.equals(other.url)) {
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
    return "HarRequest [method=" + method + ", url=" + url + ", httpVersion=" + httpVersion + ", headers=" + headers + ", queryString=" + queryString + ", postData=" + postData + "]";
  }

}
