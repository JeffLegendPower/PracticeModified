package me.trixxtraxx.Practice;

import com.grinderwolf.swm.api.SlimePlugin;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.ComponentEditor.ComponentEditor;
import me.trixxtraxx.Practice.GameLogic.Components.Components.*;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components.OpponentPlaceholderComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components.PointComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelSpawnComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogicListener;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.*;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings.SettingsComponent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers.DropToBlockinTimer;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers.DropToBreakTimer;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers.DropToResetTimer;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloSpawnCoponent;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Kit.Editor.KitEditor;
import me.trixxtraxx.Practice.Kit.Editor.KitEditorListener;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Lobby.LobbyListener;
import me.trixxtraxx.Practice.Map.Components.BedLayerComponent;
import me.trixxtraxx.Practice.Map.Components.BreakRegion;
import me.trixxtraxx.Practice.Map.Components.ClearOnDropComponent;
import me.trixxtraxx.Practice.Map.Components.NoMapBreakComponent;
import me.trixxtraxx.Practice.Map.Editor.MapEditingSession;
import me.trixxtraxx.Practice.Map.Editor.MapEditorListener;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.SQL.CacheListener;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.Utils.Region;
import me.trixxtraxx.Practice.worldloading.SlimeWorldLoader;
import me.trixxtraxx.Practice.worldloading.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
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
    // - bow boost practice
    // - fb jump practice
    // - bed build practice
    // FFA:
    // - gun games? like infinite gun game lobby you can just join
    // - one in a chamber
    // - parties/events
    // Duels:
    // - Pot pvp
    // - uhc
    // - sumo
    // - meh would have to look at other servers
    // - skywars duels
    // - combo
    // - boxxing
    // - Bed Fight
    // Teams:
    // - Invis Practice
    // - Top Bridge fight


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
        getServer().getPluginManager().registerEvents(new CacheListener(), this);
        getServer().getPluginManager().registerEvents(new LobbyListener(), this);
        
        new Lobby(conf.getConfigurationSection("Lobby"));
        
        if (conf.getBoolean("KitEditor.enabled"))
        {
            log(3, "enabled kit editor");
            getServer().getPluginManager().registerEvents(new KitEditorListener(), this);
            //init kit editor
            KitEditor.init(new Region(new Location(Bukkit.getWorld("world"), conf.getInt("KitEditor.Region.x1"), conf.getInt("KitEditor.Region.y1"), conf.getInt("KitEditor.Region.z1")), new Location(Bukkit.getWorld("world"), conf.getInt("KitEditor.Region.x2"), conf.getInt("KitEditor.Region.y2"), conf.getInt("KitEditor.Region.z2"))));
        }
        else log(3, "disabled kit editor");
        
        if(conf.getBoolean("MapEditor.enabled")){
            log(3, "enabled map editor");
            getServer().getPluginManager().registerEvents(new MapEditorListener(), this);
        }
        
        if(conf.getBoolean("ComponentEditor.enabled")){
            log(3, "enabled component editor");
            ComponentEditor.init(conf);
        }
    
        BungeeUtil.getInstance().init(conf.getString("Bungee.Identifier"), conf.getInt("Bungee.maxRatedPlayers"));
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
            if(args[0].equalsIgnoreCase("Blockin"))
            {
                Player p = (Player) s;
                //Map m = new Map(1,"Blockin1", "TestMap", new SoloSpawnCoponent(new Location(p.getWorld(), 0, 108, 0)));
                Map m = SQLUtil.Instance.getMap("Blockin1");
                Kit k = SQLUtil.Instance.getKit(args[1]);
                SQLUtil.Instance.applyComponents(k);
                Game g = new Game(new SoloGameLogic(), new List<Player>(Collections.singletonList(p)), k, m);
                g.getLogic().setName("BlockinPractice");
                new BreakResetComponent(g.getLogic(), Material.BED_BLOCK);
                new MapResetComponent(g.getLogic());
                new SettingsComponent(g.getLogic(),Material.NETHER_STAR,ChatColor.AQUA + "Settings");
                new YKillComponent(g.getLogic(), 50);
                new KillResetComponent(g.getLogic());
                new DisconnectStopComponent(g.getLogic());
                new DropItemComponent(g.getLogic(), Material.ANVIL, new List<>(new Material[]{Material.ANVIL, Material.BED,Material.NETHER_STAR}));
                new StartInventoryComponent(g.getLogic());
                new InventoryOnResetComponent(g.getLogic());
                new DropToBlockinTimer(g.getLogic());
                new DropToResetTimer(g.getLogic());
                new DropToBreakTimer(g.getLogic(), Material.WOOL);
                new DropToBreakTimer(g.getLogic(), Material.WOOD);
                new DropToBreakTimer(g.getLogic(), Material.ENDER_STONE);
                new ScoreboardComponent(g.getLogic(),
                        ChatColor.AQUA + "Blockin Practice",
                                "" + "\n" +
                                ChatColor.BLUE + "Wool:         " + ChatColor.AQUA + "{WOOLTimer}" + "\n" +
                                ChatColor.BLUE + "Wood:        " + ChatColor.AQUA + "{WOODTimer}" + "\n" +
                                ChatColor.BLUE + "Endstone: " + ChatColor.AQUA + "{ENDER_STONETimer}" + "\n" +
                                ChatColor.BLUE + "Blockin:     " + ChatColor.AQUA + "{BlockinTimer}" + "\n" +
                                ChatColor.BLUE + "Finish:       " + ChatColor.AQUA + "{TotalTimer}" + "\n" +
                                "" + "\n" +
                                ChatColor.BLUE + "Ranked.fun" + "\n"
                );
                
                SQLUtil.Instance.applyComponents(m);
                
                /*new BedLayerComponent(m, Material.ENDER_STONE, new ConfigLocation(0, 101, 0), new ConfigLocation(1, 101, 0), 1, true);
                new BedLayerComponent(m, Material.WOOD, new ConfigLocation(0, 101, 0), new ConfigLocation(1, 101, 0), 2, false);
                new BedLayerComponent(m, Material.WOOL, new ConfigLocation(0, 101, 0), new ConfigLocation(1, 101, 0), 3, false);
                new NoMapBreakComponent(m);
                new ClearOnDropComponent(m, new Region(new Location(p.getWorld(), -5, 107, -5), new Location(p.getWorld(), 4, 112, 4)));
                new BreakRegion(m, new Region(new Location(p.getWorld(), -3, 101, 3), new Location(p.getWorld(), 4, 104, -3)), true);*/
            }
            else if(args[0].equalsIgnoreCase("Bridge"))
            {
                Player p = (Player) s;
                Map m = new Map(-1,"BridgeTest", "BridgeTest", new SoloSpawnCoponent(new Location(p.getWorld(), 0, 100, 0)));
                Kit k = SQLUtil.Instance.getKit(args[1]);
                SQLUtil.Instance.applyComponents(k);
                Game g = new Game(new SoloGameLogic(), new List<>(Collections.singletonList(p)), k, m);
                g.getLogic().setName("BridgePractice");
                new PressurePlateResetComponent(g.getLogic());
                new MapResetComponent(g.getLogic());
                new YKillComponent(g.getLogic(), 90);
                new KillResetComponent(g.getLogic());
                new DisconnectStopComponent(g.getLogic());
                new DropItemComponent(g.getLogic(), Material.WOOL, new List<>(new Material[]{Material.BED}));
                new StartInventoryComponent(g.getLogic());
                new InventoryOnResetComponent(g.getLogic());
                new DropToResetTimer(g.getLogic());

                new NoMapBreakComponent(m);
            }
            else if(args[0].equalsIgnoreCase("classic"))
            {
                Player p = (Player) s;
                Player p2 = Bukkit.getPlayer(args[1]);
                DuelSpawnComponent spawn = new DuelSpawnComponent();
                Map m = new Map(-1,"Practice1", "Practice1", spawn);
                Kit k = SQLUtil.Instance.getKit(args[2]);
                SQLUtil.Instance.applyComponents(k);
                Game g = new Game(new DuelGameLogic(), new List<>(Arrays.asList(p,p2)), k,m);
                g.getLogic().setName("Classic");
                new YKillComponent(g.getLogic(), 90);
                new NoDieComponent(g.getLogic());
                new DisconnectStopComponent(g.getLogic());
                new DieStopComponent(g.getLogic());
                new StartInventoryComponent(g.getLogic());
                new SpawnProtComponent(g.getLogic(), 100,
                        ChatColor.BLUE + "The Game starts in "+ ChatColor.AQUA +"{Timer}s",
                        ChatColor.BLUE + "The Game started, go fight!"
                );
                new ScoreboardComponent(g.getLogic(),
                        ChatColor.AQUA + "Classic",
                                "" + "\n" +
                                ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" +
                                "" + "\n" +
                                ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" +
                                ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" +
                                "" + "\n" +
                                ChatColor.BLUE + "Ranked.fun" + "\n"
                );
                new PlayerPlaceholderComponent(g.getLogic());
                new MapNamePlaceholderComponent(g.getLogic());
                new OpponentPlaceholderComponent(g.getLogic());

                new NoMapBreakComponent(m);
            }
            else if(args[0].equalsIgnoreCase("sumo"))
            {
                Player p = (Player) s;
                Player p2 = Bukkit.getPlayer(args[1]);
                DuelSpawnComponent spawn = new DuelSpawnComponent(3, 100, 0, 90, 0, -3, 100, 0, -90, 0);
                Map m = new Map(-1, "Sumo1", "Sumo1", spawn);
                //Kit k = SQLUtil.Instance.getKit(args[2]);
                Kit k = new Kit("Sumo1", -1, new List<>(), -1, new HashMap<>());
                //SQLUtil.Instance.applyComponents(k);
                Game g = new Game(new DuelGameLogic(), new List<>(p,p2), k, m);
                g.getLogic().setName("Sumo");
                new YKillComponent(g.getLogic(), 97);
                new NoDieComponent(g.getLogic());
                new DisconnectStopComponent(g.getLogic());
                new DieStopComponent(g.getLogic());
                new StartInventoryComponent(g.getLogic());
                new SpawnProtComponent(g.getLogic(), 100,
                        ChatColor.BLUE + "The Game starts in "+ ChatColor.AQUA +"{Timer}s",
                        ChatColor.BLUE + "The Game started, go fight!"
                );
                new ScoreboardComponent(g.getLogic(),
                        ChatColor.AQUA + "Sumo",
                                "" + "\n" +
                                ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" +
                                "" + "\n" +
                                ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" +
                                "{Points1}" + "\n" +
                                ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" +
                                "{Points2}" + "\n" +
                                "" + "\n" +
                                ChatColor.BLUE + "Ranked.fun" + "\n"
                );
                new PlayerPlaceholderComponent(g.getLogic());
                new MapNamePlaceholderComponent(g.getLogic());
                new OpponentPlaceholderComponent(g.getLogic());
                new PointComponent(g.getLogic(), 2, "⬤");

                new NoMapBreakComponent(m);
            }
            else if(args[0].equalsIgnoreCase("saveKit"))
            {
                Player p = (Player) s;
                PlayerInventory inv = p.getInventory();
                HashMap<Integer, Integer> defaultOrder = new HashMap<>();
                List<ItemStack> items = new List<>();
                for(int i = 0; i < 40; i++)
                {
                    ItemStack item = inv.getItem(i);
                    if(item == null) continue;
                    defaultOrder.put(items.size(), i);
                    items.add(item);
                }
                Kit k = new Kit(args[1], -1, items, -1, defaultOrder);
                SQLUtil.Instance.addKit(k);
            }
            else if(args[0].equalsIgnoreCase("saveMap"))
            {
                Player p = (Player) s;
                Game g = Game.getGame(p);
                SQLUtil.Instance.addMap(g.getLogic().getMap());
            }
            else if(args[0].equalsIgnoreCase("saveGamemode"))
            {
                Player p = (Player) s;
                Game g = Game.getGame(p);
                SQLUtil.Instance.addLogic(g.getLogic());
            }
            else if(args[0].equalsIgnoreCase("deleteKit"))
            {
                SQLUtil.Instance.deleteKit(SQLUtil.Instance.getKit(args[1]));
            }
            else if(args[0].equalsIgnoreCase("loadGamemode"))
            {
                Player p = (Player) s;
                Map m = SQLUtil.Instance.getMap(args[2]);
                Kit k = SQLUtil.Instance.getKit(args[3]);
                SQLUtil.Instance.applyComponents(k);
                SQLUtil.Instance.applyComponents(m);

                List<Player> ppl = new List<>();

                ppl.add(p);

                for (int i = 4;i < args.length; i++)
                {
                    ppl.add(Bukkit.getPlayer(args[i]));
                }

                Game g = new Game(SQLUtil.Instance.getLogic(args[1]), ppl,k,m);
                SQLUtil.Instance.applyComponents(g.getLogic());
            }
            else if(args[0].equalsIgnoreCase("editMap"))
            {
                if(!(s instanceof Player)) return false;
                Map m = SQLUtil.Instance.getMap(args[1]);
                SQLUtil.Instance.applyComponents(m);
                MapEditingSession.addSession(new MapEditingSession(((Player) s), m));
            }
            else if(args[0].equalsIgnoreCase("openMapComponents"))
            {
                if(!(s instanceof Player)) return false;
                MapEditingSession session = MapEditingSession.getSession(((Player) s));
                if(session == null) return false;
                session.openComponentGui();
            }
            else if(args[0].equalsIgnoreCase("setConfigItem"))
            {
                if(!(s instanceof Player)) return false;
                log(4, "Setting config item: " + args[1] + " to " + ((Player)s).getInventory().getItemInHand());
                getConfig().set(args[1], ((Player)s).getInventory().getItemInHand());
                ComponentEditor.init(getConfig());
                try
                {
                    getConfig().save("./plugins/"+ getPlugin(Practice.class).getName() +"/config.yml");
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
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
