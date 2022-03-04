package me.trixxtraxx.bwp;

import org.bukkit.plugin.java.JavaPlugin;

public final class BWP extends JavaPlugin
{
    public static BWP Instance;
    private static int loglevel;

    // Stats:
    // Store it in a table, make a new table for each Gamemode. stats will be handles by the GAMEMODE EXTENSIONS!
    //
    // Gamemodes:
    // have it implement a abstract class that does:
    // - joining/leaving
    // - world loading
    // - Winning
    // - Scoreboard, custimizeable, make a function to apply placeholders in the implementations tho, use Fastboard library its good
    // - kits, including hotbar configurations. Add kits to the config, make a sql storing methode for kits and a kit editor!
    //   Hotbar configurations will be available for all kits including custom ones
    // - modifiers: double jump, map break, combo, whatever we can come up with, custimizable in the KIT
    //
    // this class is also extended by presets:
    // - solo practice:
    //      handles map resets
    //      handles win condition //reach a region, step on a pressure plate, break a block...
    // - Duel - 1v1 to the death
    //
    // - x teams with x players, respawn mechanic TEAMS CAN BE DIFFERENT SIZES FOR EXAMPLE 1v4
    // - FFA
    // - Point Duels (for The Bridge, mlg rush etc.)
    // - Point System with x teams of x Players
    // you can extend these classes in order to make your own Gamemode, they just function as a base
    //
    // GAMEMODES WILL BE STORED IN MYSQL, YOU WILL BE ABLE TO GET IT FROM A CONFIG FILE HOWEVER!
    // all slime worlds should also be stored in mysql for easier scalability!
    // all the extensions should also store their data in mysql rather then in files, this is so you can easly create custom Gamemodes in the kit editor
    // make a import from file function to import Gamemodes...
    //
    // Kits:
    // - Kit editor inside a region or in a new world, probably inside a region, you can still have a tp to another world entering the region like that
    // - user kits stored in mysql
    // - Kits can choose a Gamemode and modify all aspects of it...
    //
    // PVP Bots:
    // - Pvp Bots, we will see where that goes lmao
    //
    // Joining:
    // make a SPIGOT join System that works with x practice lobby servers
    // servers can have multiple Gamemodes running
    // lobbies broadcast their queueing state
    // game servers broadcast their playing state
    // when a lobby starts up it broadcasts a message requesting all server states in a seperate channel
    //
    // GUIS:
    // - play unranked
    // - play ranked
    // - leaderboard
    // - stats (per Gamemode, different for each Gamemode extension)
    //
    // make an extensive api in order to create Gamemodes Quick

    @Override
    public void onEnable()
    {
        Instance = this;
        saveDefaultConfig();
        loglevel = getConfig().getInt("LogLevel");

    }

    @Override
    public void onDisable()
    {

    }

    public static void log(int lvl, String msg)
    {
        if(loglevel >= lvl)
        {
            if(lvl == 5)
            {
                Instance.getLogger().info("[" + "\u001B[37mULTRA DEBUG" + "\u001B[0m] " + msg);
            }
            if(lvl == 4)
            {
                Instance.getLogger().info("[" + "\u001B[35mDEBUG" + "\u001B[0m] " + msg);
            }
            if(lvl == 3)
            {
                Instance.getLogger().info("[" + "\u001B[34mInformation" + "\u001B[0m] " + msg);
            }
            if(lvl == 2)
            {
                Instance.getLogger().info("[" + "\u001B[33mWarning" + "\u001B[0m] " + msg);
            }
            if(lvl == 1)
            {
                Instance.getLogger().info("[" + "\u001B[31mError" + "\u001B[0m] " + msg);
            }
        }
    }
}
