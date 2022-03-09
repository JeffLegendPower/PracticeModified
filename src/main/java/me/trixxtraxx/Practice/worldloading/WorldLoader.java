package me.trixxtraxx.Practice.worldloading;

import org.bukkit.World;

public interface WorldLoader
{
    public World loadWorld(String name);
    public void unloadWorld(World world);
}
