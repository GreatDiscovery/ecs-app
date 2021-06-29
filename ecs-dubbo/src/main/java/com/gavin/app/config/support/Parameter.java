package com.gavin.app.config.support;

import java.lang.annotation.*;

/**
 * @author: Gavin
 * @date: 2021/6/29 18:05
 * @description:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Parameter {

    String key() default "";

    boolean required() default false;

    boolean excluded() default false;

    boolean escaped() default false;

    boolean attribute() default false;

    boolean append() default false;

    /**
     * if {@link #key()} is specified, it will be used as the key for the annotated property when generating url.
     * by default, this key will also be used to retrieve the config value:
     * <pre>
     * {@code
     *  class ExampleConfig {
     *      // Dubbo will try to get "dubbo.example.alias_for_item=xxx" from .properties, if you want to use the original property
     *      // "dubbo.example.item=xxx", you need to set useKeyAsProperty=false.
     *      @Parameter(key = "alias_for_item")
     *      public getItem();
     *  }
     * }
     *
     * </pre>
     */
    boolean useKeyAsProperty() default true;

}
