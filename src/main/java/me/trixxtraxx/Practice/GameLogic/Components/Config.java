package me.trixxtraxx.Practice.GameLogic.Components;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config
{
    String description() default "This is a property, it can do a lot of stuff!";
}
