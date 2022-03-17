package me.trixxtraxx.Practice.Kit.Editor;

import me.trixxtraxx.Practice.Utils.Region;

public class KitEditor
{
    private static KitEditor instance;
    private Region region;



    private KitEditor(Region r){region = r;}
    public static void init(Region r)
    {
        if(hasInstance()) return;
        instance = new KitEditor(r);
    }
    public static boolean hasInstance(){return instance != null;}
    public static KitEditor getInstance(){return instance;}
}