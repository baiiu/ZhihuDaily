package com.baiiu.interfaces.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: zhuzhe
 * time: 2020-04-21
 * description: 模仿AutoService & WMRouter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouterService {


    Class<?>[] value();

}
