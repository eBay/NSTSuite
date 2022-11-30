package com.ebay.nst.coverage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
/**
 * Used to mark classes and methods so they are not included in code coverage metrics.
 */
public @interface Generated {}
