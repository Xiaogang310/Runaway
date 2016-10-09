package com.cyou.runaway.inhouse.Core.Annotation.Doc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Gang on 2016/10/9.
 * 文档方法注释，用于生成文档
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodAnnotation
{
    enum Method_Type
    {
        MT_CALL,
        MT_GET,
        MT_POST,
    }

    public String description() default "";

    public Method_Type type();

    public String[] params() default {};

    public String result() default "void";
}