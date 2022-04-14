package me.trixxtraxx.Practice;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TriggerEvent
{
    int priority() default 0;
    CancelState state() default CancelState.NONE;
    public enum CancelState
    {
        NONE,
        ENSURE_CANCEL,
        ENSURE_NOT_CANCEL
    }
}
