package com.jtestrunner.core;


import com.jtestrunner.model.ModuleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yusufaltun on 04/04/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PageType
{
    String url() default "";
    boolean useAbsolutePath() default true;
    ModuleType module() default ModuleType.STOREFRONT;
}
