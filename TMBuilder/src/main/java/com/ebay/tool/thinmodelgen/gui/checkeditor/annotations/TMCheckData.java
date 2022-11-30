package com.ebay.tool.thinmodelgen.gui.checkeditor.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for applying to TM setters. Please do not apply this to getters as
 * those will be identified based on the setter or by the corresponding
 * annotation property. This annotation is used to generate the UI input
 * elements for the methods decorated with this annotation.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface TMCheckData {

  /**
   * Input name - always shown to user.
   *
   * @return Name.
   */
  String inputName();

  /**
   * Input description - shown to the user to help them better understand the
   * input.
   *
   * @return Description.
   */
  String inputDescription();

  /**
   * By default, all methods with the annotation applied, are used for
   * populating input UI. If this is set to false, the method will be skipped.
   *
   * @return True to use in input UI, false otherwise.
   */
  boolean enabled() default true;

  /**
   * Corresponding getters are matched by method name. If the method name
   * difference between getter and setter is greater than just the get/set
   * prefix then this field should be used to provide the corresponding getter
   * method name.
   *
   * @return Unique getter method name, or empty string if not needed.
   */
  String getterMethodName() default "";
}
