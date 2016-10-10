package com.cyou.runaway.inhouse.Core.Annotation.Doc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Gang on 2016/10/9.
 * 文档Field注释，用于生成文档
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface FieldAnnotation
{

}
