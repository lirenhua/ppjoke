package com.lrh.libnavnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by LRH on 2020/11/15 0015
 */
@Target(ElementType.TYPE)
public @interface FragmentDestination {
    String pageUrl();

    boolean needLogin() default false;

    boolean asStarter() default false;
}
