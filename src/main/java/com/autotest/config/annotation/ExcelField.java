package com.autotest.config.annotation;

import org.codehaus.jackson.annotate.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface ExcelField {
    String name() default "";

    int position();

    boolean enabled() default true;

    String description() default "";
}
