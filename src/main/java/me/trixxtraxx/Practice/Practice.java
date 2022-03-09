package me.trixxtraxx.Practice;

import com.grinderwolf.swm.api.SlimePlugin;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.*;
import me.trixxtraxx.Practice.GameLogic.Components.Components.DisconnectStopComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.StartInventoryComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.YKillComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogicListener;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloSpawnCmponent;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.Components.BreakRegion;
import me.trixxtraxx.Practice.Map.Components.NoMapBreakComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import me.trixxtraxx.Practice.Utils.Region;
import me.trixxtraxx.Practice.worldloading.SlimeWorldLoader;
import me.trixxtraxx.Practice.worldloading.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public final class Practice extends JavaPlugin
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

    // Gamemode List:
    // Solo:
    // - Blocking Practice
    // - Bridge Practice
    // FFA:
    // - gun games? like infinite gun game lobby you can just join
    // - parties/events
    // Duels:
    // - Pot pvp
    // - uhc
    // - sumo
    // - meh would have to look at other servers
    // - skywars duels
    // Teams:
    // - Invis Practice
    // - Top Bridge fight or whatever


    public static Practice Instance;
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
        if (label.equalsIgnoreCase("TestGame"))
        {
            Player p = (Player) s;
            Map m = new Map("MapName", "TestMap", new SoloSpawnCmponent(new Location(p.getWorld(), 0, 108, 0)));
            HashMap<Integer, Integer> order = new HashMap<>();
            order.put(0,2);
            order.put(1,3);
            order.put(2,8);
            order.put(3,6);
            order.put(4,7);
            order.put(5,1);
            Kit k = new Kit(Arrays.asList(new ItemStack[]
                    {
                            new ItemStack(Material.IRON_AXE),
                            new ItemStack(Material.WOOL, 64),
                            new ItemStack(Material.IRON_PICKAXE),
                            new ItemStack(Material.SHEARS),
                            new ItemStack(Material.ANVIL),
                            new ItemStack(Material.BED)
                    }), order);
            Game g = new Game(new SoloGameLogic(m), Collections.singletonList(p), k);
            new BreakResetComponent(g.getLogic(), Material.BED_BLOCK);
            new MapResetComponent(g.getLogic());
            new YKillComponent(g.getLogic(), 50);
            new KillResetComponent(g.getLogic());
            new DisconnectStopComponent(g.getLogic());
            new DropItemComponent(g.getLogic(), Material.ANVIL, Arrays.asList(new Material[]{Material.ANVIL, Material.BED}), new Region(new Location(p.getWorld(), -5, 107, -5), new Location(p.getWorld(), 4, 112, 4)));
            new StartInventoryComponent(g.getLogic());
            new InventoryOnResetComponent(g.getLogic());
            new NoMapBreakComponent(m);
            new BreakRegion(m, new Region(new Location(p.getWorld(), -3, 101, 3), new Location(p.getWorld(), 4, 104, -3)), true);
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
