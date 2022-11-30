package com.ebay.runtime.event;

import java.util.Objects;

public class ObserverPayload {

  private String className;
  private String methodName;
  
  public ObserverPayload(String className, String methodName) {
	  this.className = Objects.requireNonNull(className, "ClassName MUST NOT be null.");
	  this.methodName = Objects.requireNonNull(methodName, "MethodName MUST NOT be null.");
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

}
