package com.mcp.validate.annotation;


import java.lang.annotation.*;

/**
 * Created by shiqm on 2017-08-16.
 */


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Check {

    String value() default "";

    boolean required() default true;

    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";

    String min() default "";

    String max() default "";

    String pattern() default "";

    String name() default "";

    boolean mobile() default false;

    boolean email() default false;

    boolean idCard() default false;

    boolean isDecimal() default false;

    boolean numOrLetter() default false;

    boolean chinese() default false;

    boolean ip() default false;

    boolean url() default false;

    boolean date() default false;

    boolean number() default false;

    boolean notZero() default false;

    boolean letter() default false;

    int length() default 0;

    Class[] valid() default {};

}
