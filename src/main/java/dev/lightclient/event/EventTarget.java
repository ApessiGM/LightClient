package dev.lightclient.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event handler. The method must declare exactly one
 * parameter whose type is a subclass of {@link Event}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventTarget {
    /** Higher priority handlers run first. */
    int priority() default 0;
}
