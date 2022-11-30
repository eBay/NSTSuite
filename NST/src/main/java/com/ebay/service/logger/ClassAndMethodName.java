package com.ebay.service.logger;

import java.util.Objects;

public class ClassAndMethodName {

	private String className;
	private String methodName;

	public ClassAndMethodName(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, methodName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassAndMethodName other = (ClassAndMethodName) obj;
		return Objects.equals(className, other.className) && Objects.equals(methodName, other.methodName);
	}

	@Override
	public String toString() {
		return "ClassAndMethodName [className=" + className + ", methodName=" + methodName + "]";
	}

}
