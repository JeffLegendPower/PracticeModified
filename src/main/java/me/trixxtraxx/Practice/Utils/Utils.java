package me.trixxtraxx.Practice.Utils;

public class Utils
{
    public static Class<?> getClass(String name)
    {
        try {
            Class<?> act = Class.forName("com.bla.TestActivity");
            return act;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
