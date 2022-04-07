package me.trixxtraxx.Practice;

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
