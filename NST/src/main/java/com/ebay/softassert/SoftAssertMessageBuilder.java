package com.ebay.softassert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

public class SoftAssertMessageBuilder {

  private List<String> ignoreClasses = new ArrayList<String>();

  /**
   * Add a class signature to be ignored by the message builder. Example:
   * com.ebay.service.logger.ResponseLogger
   *
   * @param classSignature
   *          Class signature to be ignored.
   */
  public void ignoreClass(String classSignature) {
    ignoreClasses.add(classSignature);
  }

  /**
   * Build the message by wrapping it with the stack trace.
   * @param message Assert message to wrap.
   * @return Wrapped assert message.
   */
  public String buildMessage(String message) {
    String signature = getSignature();
    return String.format("%s : %s -->", message, signature);
  }

  private String getSignature() {

    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StringBuilder signatureBuilder = new StringBuilder();

    for (int i = 1; i < stackTrace.length; i++) {

      StackTraceElement traceElement = stackTrace[i];
      String className = traceElement.getClassName();
      String methodName = traceElement.getMethodName();
      int lineNumber = traceElement.getLineNumber();

      if (methodName.equalsIgnoreCase("<init>")) {
        continue;
      } else if (ignoreClasses.contains(className)) {
        continue;
      }

      if (!className.equals(SoftAssertMessageBuilder.class.getName()) && className.indexOf("java.lang.Thread") != 0) {

        if (signatureBuilder.length() > 0) {
          signatureBuilder.insert(0, " > ");
        }
        signatureBuilder.insert(0, String.format("[%s.%s() - ln %d]", className, methodName, lineNumber));

        Class<?> classMatch = null;
        try {
          classMatch = Class.forName(className);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
          return "ClassNotFoundException - unresolvable stack message.";
        }
        Method[] methods = classMatch.getMethods();
        boolean matched = false;
        for (Method method : methods) {
          if (method.getName().equals(methodName)) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
              if (annotation instanceof Test) {
                matched = true;
                break;
              }
            }
            if (matched) {
              break;
            }
          }
        }

        if (!matched) {
          continue;
        }

        return signatureBuilder.toString();
      }
    }

    return "unresolved stack trace";
  }
}
