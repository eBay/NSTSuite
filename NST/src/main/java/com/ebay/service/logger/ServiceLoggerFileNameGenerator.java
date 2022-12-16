package com.ebay.service.logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

public class ServiceLoggerFileNameGenerator {

  private List<String> ignoreClasses = new ArrayList<>();
  private HashMap<String, Integer> requestClassAndMethodKeyToIndexMap = new HashMap<>();
  private HashMap<String, Integer> responseClassAndMethodKeyToIndexMap = new HashMap<>();

  private static ServiceLoggerFileNameGenerator instance;

  private ServiceLoggerFileNameGenerator() {

  }

  public static ServiceLoggerFileNameGenerator getInstance() {

    if (instance == null) {
      synchronized (ServiceLoggerFileNameGenerator.class) {
        if (instance == null) {
          instance = new ServiceLoggerFileNameGenerator();
        }
      }
    }

    return instance;
  }

  /**
   * Clear the map of remembered file names.
   */
  public void clear() {
    requestClassAndMethodKeyToIndexMap.clear();
    responseClassAndMethodKeyToIndexMap.clear();
  }

  /**
   * Add a class signature to be ignored in the reporting. Example:
   * com.ebay.service.logger.ResponseLogger
   *
   * @param classSignature
   *          Class signature to be ignored.
   */
  public void ignoreClass(String classSignature) {
    ignoreClasses.add(classSignature);
  }

  /**
   * Get the calling class and method signature combined in a single string. For
   * use with file names. Looks for TestNG test annotations applied to the
   * method and uses the first found to build the file name from. Returns
   * ClassName_MethodName_site_index_forcedServiceName.
   *
   * @param serviceWrapperApiName
   *          Force the inclusion of a service name.
   * @return Combined String, or null if unable to find a TestNG test method.
   */
  public String getCallingClassAndMethodSignature(String serviceWrapperApiName) {

    String keySignature = getSignature();
    int index = 1;

    if (!responseClassAndMethodKeyToIndexMap.containsKey(keySignature)) {
      responseClassAndMethodKeyToIndexMap.put(keySignature, index);
    } else {
      index = responseClassAndMethodKeyToIndexMap.get(keySignature);
      index++;
      responseClassAndMethodKeyToIndexMap.put(keySignature, index);
    }

    return String.format("%s_%d_%s", keySignature, index, serviceWrapperApiName);
  }

  /**
   * Get the last calling class and method signature combined in a single string
   * used for the calling class and method. Use for file names. This WILL NOT
   * update the index and should ONLY be used to retrieve the last file name
   * indexed.
   *
   * @param serviceWrapperApiName
   *          Force the inclusion of a service name.
   * @return Combined String, or null if unable to find a TestNG test method or
   *         record does not exist in internal map.
   */
  public String getLatestCallingClassAndMethodSignatureIndexed(String serviceWrapperApiName) {

    String keySignature = getSignature();

    if (responseClassAndMethodKeyToIndexMap.containsKey(keySignature)) {
      int index = responseClassAndMethodKeyToIndexMap.get(keySignature);
      return String.format("%s_%d_%s", keySignature, index, serviceWrapperApiName);
    }

    return null;
  }

  /**
   * Get the class and method name of the test that is running. You are likely
   * looking for getCallingClassAndMethodSignature to create the filename.
   *
   * @return Class and method name instance.
   */
  public ClassAndMethodName getClassAndMethodName() {

    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    for (int i = 1; i < stackTrace.length; i++) {

      StackTraceElement traceElement = stackTrace[i];
      String className = traceElement.getClassName();
      String methodName = traceElement.getMethodName();

      if (ignoreClasses.contains(className)) {
        continue;
      }

      if (!className.equals(ServiceLoggerFileNameGenerator.class.getName()) && className.indexOf("java.lang.Thread") != 0) {

        Class<?> classMatch;
        try {
          classMatch = Class.forName(className);
        } catch (ClassNotFoundException e) {
          return null;
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

        className = className.substring(className.lastIndexOf(".") + 1);
        return new ClassAndMethodName(className, methodName);
      }
    }

    return null;
  }

  private String getSignature() {

    ClassAndMethodName classAndMethodName = getClassAndMethodName();
    String keySignature = String.format("%s_%s", classAndMethodName.getClassName(), classAndMethodName.getMethodName());
    return keySignature;
  }

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
  }
}
