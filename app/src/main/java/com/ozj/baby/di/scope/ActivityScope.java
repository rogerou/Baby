package com.ozj.baby.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Roger on 2016/4/13.
 * <p>
 * 控制Activity的生命周期
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {


}
