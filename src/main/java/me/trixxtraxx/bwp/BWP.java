package me.trixxtraxx.bwp;

import com.grinderwolf.swm.api.SlimePlugin;
import me.trixxtraxx.bwp.GameLogic.Components.Components.DisconnectStopComponent;
import me.trixxtraxx.bwp.GameLogic.Components.Components.YKillComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogicListener;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components.BreakResetComponent;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components.DropItemComponent;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components.KillResetComponent;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components.MapResetComponent;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.SoloSpawnCmponent;
import me.trixxtraxx.bwp.Gamemode.Game;
import me.trixxtraxx.bwp.Kit.Kit;
import me.trixxtraxx.bwp.Map.Components.BreakRegion;
import me.trixxtraxx.bwp.Map.Components.NoMapBreakComponent;
import me.trixxtraxx.bwp.Map.Components.PlaceRegion;
import me.trixxtraxx.bwp.Map.Map;
import me.trixxtraxx.bwp.SQL.SQLUtil;
import me.trixxtraxx.bwp.Utils.Region;
import me.trixxtraxx.bwp.worldloading.SlimeWorldLoader;
import me.trixxtraxx.bwp.worldloading.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

public final class BWP extends JavaPlugin
{
    // Stats:
    // Store it in a table, make a new table for each Gamemode. stats will be handles by the GAMEMODE EXTENSIONS!
    //
    // Gamemodes:
    // THIS IS THE MAIN GAME CLASS!
    // - joining/leaving
    // - world loading
    // - COMPONENT SYSTEM,
    // - Winning
    // - Scoreboard, custimizeable, make a function to apply placeholders in the implementations tho, use Fastboard library its good //COMPONENT
    // - kits, including hotbar configurations. Add kits to the config, make a sql storing methode for kits and a kit editor!
    //   Hotbar configurations will be available for all kits including custom ones
    // - modifiers: double jump, map break, combo, whatever we can come up with, custimizable in the KIT //COMPONENT
    // - provides common functions like map resets, teams, respawning, etc... they can be overwritten by the gamemode extensions //COMPONENT
    // - point System within teams, OPTIONAL usage //COMPONENT
    // add it if I forgot something, here and in the class
    //
    //
    // HANDLED BY ALL GAMEPLAY LOOPS:
    // - map configurations
    //
    // There will be a GAMELOOP Component, that handles the general GAMELOOP:
    // - solo practice:
    //      handles SQL
    //      handles win condition //reach a region, step on a pressure plate, break a block...
    // - Duel - 1v1 to the death
    //      handles SQL
    // - x teams with x players, respawn mechanic TEAMS CAN BE DIFFERENT SIZES FOR EXAMPLE 1v4
    //      handles teams
    //      uses a general purpose team class
    //      handles respawn mechanic, toggleable // no_respawn, respawn without final chance, respawn with final chance like Bedwars
    //      handles integrations with Bedwars
    // - FFA
    //      a mode that works like infinite gun games, that people can join and leave whenever, TOGGLEABLE
    //      everyone vs everyone
    //      same as duels but with more players
    // you can extend these classes in order to make your own Gamemode, they just function as a base
    // add it if I forgot something, here and in the class
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

    // NEW:
    //
    // 4 Components play together: Gamemode, Gameloop, Map, Kit
    //
    // Gamemode:
    // - by default it handles only very basic stuff
    // - you can add components too it, which makes it custimizable
    // - Contains a Gameloop and a kit that isnt always the same
    //
    // Gameloop:
    // - handles teams, modes are: solo, duels, x teams with x players, FFA
    // - this also has a component api, that can do something else then the Gamemode Api, for example make is bo3, add points for The Bridge...
    // - contains a map, that should get reloaded every game from sql cuz why not, runtime custimization is always good
    //
    // Map:
    // - this will handle ONLY the map loading and preperation
    // - will contains a SPAWN Component, that gets dictated by the Gameloop, different Gameloops have different spawn Components, closely tied to the gameloop
    // - Component api to place shops and other stuff
    //
    // Kits:
    // - handles inventory
    // - has a component editor

    public static BWP Instance;
    private static int loglevel;
    public static WorldLoader worldLoader;

    @Override
    public void onEnable()
    {
        Instance = this;

        saveDefaultConfig();
        FileConfiguration conf = getConfig();

        loglevel = conf.getInt("LogLevel");

        SQLUtil.Instance.init(
                conf.getString("SQL.Host"),
                conf.getString("SQL.Port"),
                conf.getString("SQL.Database"),
                conf.getString("SQL.Username"),
                conf.getString("SQL.Password")
        );
        SlimePlugin slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        worldLoader = new SlimeWorldLoader(
                slime,
                slime.getLoader(conf.getString("SlimeLoader"))
        );

        getServer().getPluginManager().registerEvents(new GameLogicListener(), this);
    }

    @Override
    public void onDisable()
    {

    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args)
    {
        if(label.equalsIgnoreCase("TestGame"))
        {
            Player p = (Player) s;
            Map m = new Map("MapName", "TestMap", new SoloSpawnCmponent(new Location(p.getWorld(), 0,108,0)));
            Game g = new Game(new SoloGameLogic(m), Collections.singletonList(p), new Kit());
            new BreakResetComponent(g.getLogic(), Material.BED_BLOCK);
            new MapResetComponent(g.getLogic());
            new YKillComponent(g.getLogic(), 50);
            new KillResetComponent(g.getLogic());
            new DisconnectStopComponent(g.getLogic());
            new DropItemComponent(
                    g.getLogic(),
                    Material.ANVIL,
                    Arrays.asList(new Material[]{Material.ANVIL, Material.BED}),
                    new Region(new Location(p.getWorld(), -5, 107, -5),new Location(p.getWorld(), 4, 112, 4)));
            new NoMapBreakComponent(m);
            new BreakRegion(m, new Region(new Location(p.getWorld(), -3,101,3),new Location(p.getWorld(), 4,104,-3)), true);
            new PlaceRegion(m, new Region(new Location(p.getWorld(), -3,101,3),new Location(p.getWorld(), 4,104,-3)), false);
        }
        return false;
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
