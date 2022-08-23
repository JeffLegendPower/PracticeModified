package me.trixxtraxx.Practice;

import com.grinderwolf.swm.api.SlimePlugin;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.RestCommunicator.PluginAPI.RegisterMessages;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.Bungee.InventoryViewListener;
import me.trixxtraxx.Practice.Bungee.Queue.QueueListener;
import me.trixxtraxx.Practice.Bungee.SpectateListener;
import me.trixxtraxx.Practice.ComponentEditor.ComponentEditor;
import me.trixxtraxx.Practice.GameLogic.GameLogicListener;
import me.trixxtraxx.Practice.Kit.Editor.Enchants.EnchantmentCategory;
import me.trixxtraxx.Practice.Kit.Editor.KitEditor;
import me.trixxtraxx.Practice.Kit.Editor.KitEditorListener;
import me.trixxtraxx.Practice.Lobby.ItemTypes.CustomGamemodeItem;
import me.trixxtraxx.Practice.Lobby.ItemTypes.EventItem;
import me.trixxtraxx.Practice.Lobby.ItemTypes.MenuItem;
import me.trixxtraxx.Practice.Lobby.ItemTypes.PartyItem;
import me.trixxtraxx.Practice.Lobby.Launchpad;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Lobby.LobbyItem;
import me.trixxtraxx.Practice.Lobby.LobbyListener;
import me.trixxtraxx.Practice.Map.Editor.MapEditorListener;
import me.trixxtraxx.Practice.SQL.CacheListener;
import me.trixxtraxx.Practice.SQL.PlayerReceiver;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.Utils.Region;
import me.trixxtraxx.Practice.config.DeepConfig;
import me.trixxtraxx.Practice.test.TestGame;
import me.trixxtraxx.Practice.worldloading.SlimeWorldLoader;
import me.trixxtraxx.Practice.worldloading.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
    // all the extensions should also store their data in mysql rather than in files, this is so you can easly create custom Gamemodes in the kit editor
    // make an import from file function to import Gamemodes...
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
    // - Contains a Gameloop and a kit that isn't always the same
    //
    // Gameloop:
    // - handles teams, modes are: solo, duels, x teams with x players, FFA
    // - this also has a component api, that can do something else then the Gamemode Api, for example make is bo3, add points for The Bridge...
    // - contains a map, that should get reloaded every game from sql cuz why not, runtime custimization is always good
    //
    // Map:
    // - this will handle ONLY the map loading and preperation
    // - will contain a SPAWN Component, that gets dictated by the Gameloop, different Gameloops have different spawn Components, closely tied to the gameloop
    // - Component api to place shops and other stuff
    //
    // Kits:
    // - handles inventory
    // - has a component editor

    // Gamemode List
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
    public static int loglevel;
    public static WorldLoader worldLoader;
    private Lobby lobby;

    @Override
    public void onEnable()
    {
        Instance = this;

        saveDefaultConfig();
        FileConfiguration conf = getConfig();
        DeepConfig deepConfig = new DeepConfig(this);

        loglevel = conf.getInt("LogLevel");

        FileConfiguration sqlConfig = deepConfig.loadConfig("sql");
        SQLUtil.Instance.init(
                sqlConfig.getString("Host"),
                sqlConfig.getString("Port"),
                sqlConfig.getString("Database"),
                sqlConfig.getString("Username"),
                sqlConfig.getString("Password")
        );
        SQLUtil.Instance.deleteOldViews();

        
        SlimePlugin slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        worldLoader = new SlimeWorldLoader(
                slime,
                slime.getLoader(conf.getString("SlimeLoader"))
        );

        FileConfiguration lobbyConfig = deepConfig.getConfig("lobby");
        lobby = initLobby(lobbyConfig);

        getServer().getPluginManager().registerEvents(new GameLogicListener(), this);
        getServer().getPluginManager().registerEvents(new CacheListener(), this);
        getServer().getPluginManager().registerEvents(new LobbyListener(lobby), this);
        getServer().getPluginManager().registerEvents(new PracticeListener(), this);



        FileConfiguration kiteditorConfig = deepConfig.loadConfig("kiteditor");
        if (kiteditorConfig.getBoolean("enabled")) {
            log(3, "enabled kit editor");
            getServer().getPluginManager().registerEvents(new KitEditorListener(), this);
            //init kit editor
            KitEditor.init(
                    new Region(new Location(Bukkit.getWorld("world"), kiteditorConfig.getInt("Region.x1"), kiteditorConfig.getInt("Region.y1"), kiteditorConfig.getInt("Region.z1")), new Location(Bukkit.getWorld("world"), kiteditorConfig.getInt("Region.x2"), kiteditorConfig.getInt("Region.y2"), kiteditorConfig.getInt("Region.z2"))),
                    kiteditorConfig.getString("world"),
                    lobby
            );
        }
        else log(3, "disabled kit editor");


    FileConfiguration mapeditorConfig = deepConfig.loadConfig("mapeditor");

        if(mapeditorConfig.getBoolean("enabled")){
            log(3, "enabled map editor");
            getServer().getPluginManager().registerEvents(new MapEditorListener(), this);
        }


        FileConfiguration componenteditorConfig = deepConfig.loadConfig("componenteditor");
        
        if(componenteditorConfig.getBoolean("enabled")){
            log(3, "enabled component editor");
            ComponentEditor.enable();
        }

        FileConfiguration bungeeConfig = deepConfig.loadConfig("bungee");
    
        BungeeUtil.getInstance().init(
                bungeeConfig.getString("Identifier"),
                bungeeConfig.getInt("maxRatedPlayers"),
                bungeeConfig.getString("lobbyServer"),
                new List<>(lobby)
                );

        RegisterMessages.registerReciever(new PlayerReceiver());
        RegisterMessages.registerReciever(new QueueListener());
        RegisterMessages.registerReciever(new InventoryViewListener());
        RegisterMessages.registerReciever(new SpectateListener());
        
        EnchantmentCategory.init();

        this.getCommand("TestGame").setExecutor(new TestGame(lobby));
    }

    @Override
    public void onDisable()
    {
        KitEditor kit = KitEditor.getInstance();
        if(kit != null)
        {
            kit.saveAll();
        }
    }

    public static void log(int lvl, String msg) {
        if(loglevel >= lvl) {
            switch (lvl) {
                case 5: Instance.getLogger().info("[" + "\u001B[37mULTRA DEBUG" + "\u001B[0m] \u001B[37m" + msg); break;
                case 4: Instance.getLogger().info("[" + "\u001B[36mDEBUG" + "\u001B[0m] \u001B[36m" + msg); break;
                case 3: Instance.getLogger().info("[" + "\u001B[35mINFO" + "\u001B[0m] \u001B[35m" + msg); break;
                case 2: Instance.getLogger().info("[" + "\u001B[34mWARNING" + "\u001B[0m] \u001B[34m" + msg); break;
                case 1: Instance.getLogger().info("[" + "\u001B[31mERROR" + "\u001B[0m] \u001B[31m" + msg); break;
            }
        }
    }

    private Lobby initLobby(FileConfiguration lobbyConfig) {

        World world = Bukkit.getWorld(lobbyConfig.getString("world"));

        List<Launchpad> launchpads = new List<>();

        ConfigurationSection launchpadSec = lobbyConfig.getConfigurationSection("launchpads");

        for (String key : launchpadSec.getKeys(false)) {
            ConfigurationSection launchpad = launchpadSec.getConfigurationSection(key);
            launchpads.add(new Launchpad(
                    ConfigLocation.deserialize(launchpad.getString("loc")),
                    launchpad.getDouble("radius"),
                    launchpad.getString("velo"),
                    world
            ));
        }

        List<LobbyItem> items = new List<>();

        ConfigurationSection ItemSec = lobbyConfig.getConfigurationSection("Items");

        for (String key : ItemSec.getKeys(false)) {
            ConfigurationSection itemSec = ItemSec.getConfigurationSection(key);
            String type = itemSec.getString("Type");

            Material material = Material.getMaterial(itemSec.getString("Item.Material"));
            String name = itemSec.contains("Item.Name") ? itemSec.getString("Item.Name") : null;
            String lore = itemSec.contains("Item.Lore") ? itemSec.getString("Item.Lore") : null;
            int slot = itemSec.getInt("Item.Slot");

            switch (type) {
                case "OPEN_MENU": items.add(new MenuItem(
                        material,
                        name,
                        lore,
                        slot,
                        itemSec.getString("Menu")
                )); break;

                case "CUSTOM_GAMEMODE": {
                    List<CustomGamemodeItem.CustomGamemode> gamemodes = new List<>();
                    ConfigurationSection gamemodeSec = itemSec.getConfigurationSection("Gamemodes");
                    for (String key2 : gamemodeSec.getKeys(false)) {
                        ConfigurationSection gamemode = gamemodeSec.getConfigurationSection(key2);

                        BetterItem item = new BetterItem(gamemode.getString("Material"));
                        if (gamemode.contains("Name")) item.setDisplayName(gamemode.getString("Name"));
                        if (gamemode.contains("Lore")) item.setLore(gamemode.getStringList("Lore"));
                        if (gamemode.contains("Data")) item.setDurability((short) gamemode.getInt("Data"));

                        gamemodes.add(new CustomGamemodeItem.CustomGamemode(
                                item,
                                key2,
                                gamemode.getString("DefaultKit"),
                                gamemode.getBoolean("Solo"),
                                new List<>(gamemode.getStringList("Maps"))
                        ));
                    }

                    items.add(new CustomGamemodeItem(
                            material,
                            name,
                            lore,
                            slot,
                            itemSec.getString("DefaultGamemode"),
                            gamemodes
                            ));
                } break;

                case "PARTIES": items.add(new PartyItem(
                        material,
                        name,
                        lore,
                        slot
                )); break;

                case "EVENTS": items.add(new EventItem(
                        material,
                        name,
                        lore,
                        slot
                )); break;
            }
        }

        return new Lobby(
                world,
                lobbyConfig.getString("name"),
                lobbyConfig.getInt("maxRatedPlayers"),
                lobbyConfig.getBoolean("canPlaceBlocks"),
                lobbyConfig.getBoolean("canBreakBlocks"),
                lobbyConfig.getBoolean("canDamageBlocks"),
                lobbyConfig.getBoolean("entityDamageEnabled"),
                lobbyConfig.getBoolean("AllowEditInventory"),
                lobbyConfig.getBoolean("canDropItems"),
                lobbyConfig.getBoolean("lockHunger"),
                lobbyConfig.getBoolean("lockWeather"),
                lobbyConfig.getInt("VoidOutHeight"),
                new List<>(lobbyConfig.getStringList("blockedInventories")),
                items,
                launchpads,
                ConfigLocation.deserialize(lobbyConfig.getString("spawn"))
        );
    }

    public Lobby getLobby() {
        return lobby;
    }
}
