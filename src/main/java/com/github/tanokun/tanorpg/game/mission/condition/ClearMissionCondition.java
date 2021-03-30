package com.github.tanokun.tanorpg.game.mission.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value=RUNTIME)
@Target(value= ElementType.TYPE)
public @interface ClearMissionCondition {
    String[] value();
}
