package com.ebay.service.logger.platforms.model;

import com.ebay.nst.coverage.Generated;

@Generated
public class GeneralPlatformOperationModel {

  private String serviceWrapperApiName;
  private StringBuilder customBlock = new StringBuilder();

  public String getServiceWrapperApiName() {
    return serviceWrapperApiName;
  }

  public GeneralPlatformOperationModel setServiceWrapperApiName(String serviceWrapperApiName) {
    this.serviceWrapperApiName = serviceWrapperApiName;
    return this;
  }

  public String getCustomBlock() {
    return customBlock.toString();
  }

  public void addCustomBlockLine(String line) {
    this.customBlock.append(line);
    this.customBlock.append("\n");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((customBlock == null) ? 0 : customBlock.hashCode());
    result = prime * result + ((serviceWrapperApiName == null) ? 0 : serviceWrapperApiName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof GeneralPlatformOperationModel)) {
      return false;
    }

    GeneralPlatformOperationModel other = (GeneralPlatformOperationModel) obj;
    if (customBlock == null) {
      if (other.customBlock != null) {
        return false;
      }
    } else if (!customBlock.toString().equals(other.customBlock.toString())) {
      return false;
    }

    if (serviceWrapperApiName == null) {
      if (other.serviceWrapperApiName != null) {
        return false;
      }
    } else if (!serviceWrapperApiName.equals(other.serviceWrapperApiName)) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "GeneralPlatformOperationModel [serviceWrapperApiName=" + serviceWrapperApiName + ", customBlock=" + customBlock + "]";
  }

}
