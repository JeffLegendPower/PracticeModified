package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.StringStorer.SQL.SQLPlayer;
import me.TrixxTraxx.StringStorer.StringStorer;
import me.trixxtraxx.Practice.Bungee.NewGamePacket;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Lobby.LobbyItem;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CustomGamemodeItem extends LobbyItem
{
    public class CustomGamemode
    {
        BetterItem item;
        String gamemode;
        String defaultKit;
        boolean solo;
        List<String> maps;

        public CustomGamemode(BetterItem item, String gamemode, String defaultKit, boolean solo, List<String> maps)
        {
            this.item = item;
            this.gamemode = gamemode;
            this.defaultKit = defaultKit;
            this.solo = solo;
            this.maps = maps;
        }

        public BetterItem getItem()
        {
            return item;
        }

        public String getGamemode()
        {
            return gamemode;
        }

        public List<String> getMaps()
        {
            return maps;
        }
        
        public String getDefaultKit()
        {
            return defaultKit;
        }
    }
    
    public class Challenge{
        Player challenger;
        Player challenged;
        String gamemode;
        String map;
        String kit;
        
        public Challenge(Player challenger, Player challenged, String gamemode, String map, String kit){
            this.challenger = challenger;
            this.challenged = challenged;
            this.gamemode = gamemode;
            this.map = map;
            this.kit = kit;
        }
        
        public void start(){
            NewGamePacket.start(new List(challenged, challenger), gamemode, map, kit, true);
        }
    }
    
    HashMap<Player, List<Challenge>> challenges = new HashMap<>();
    List<CustomGamemode> gamemodes = new List();
    String defaultMode;
    String defaultKit;
    
    public CustomGamemodeItem(ConfigurationSection section)
    {
        super(section);
        defaultMode = section.getString("DefaultGamemode");
        ConfigurationSection gms = section.getConfigurationSection("Gamemodes");
        for (String key: gms.getKeys(false))
        {
            Practice.log(4, "Loading Custom Gamemode: " + key);
            ConfigurationSection g = gms.getConfigurationSection(key);
            BetterItem i = new BetterItem(g.getString("Material"));
            if(g.contains("Name")) i.setDisplayName(g.getString("Name"));
            if(g.contains("Lore")) i.setLore(g.getStringList("Lore"));
            gamemodes.add(new CustomGamemode(
                    i,
                    key,
                    g.getString("DefaultKit"),
                    g.getBoolean("Solo"),
                    new List(g.getStringList("Maps"))
            ));
        }
        Practice.log(3, "Loaded " + gamemodes.size() + " Custom Gamemodes");
    }

    @Override
    public void onClick(PlayerInteractEvent interact)
    {
        if(interact.getAction() == Action.RIGHT_CLICK_AIR || interact.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Player p = interact.getPlayer();
            BetterInv inv = new BetterInv(p, 54, "Custom Gamemode");
            setInv(inv);
            inv.open();
        }
    }
    
    @Override
    public void onClick(EntityDamageByEntityEvent interact)
    {
        Practice.log(4, interact.getDamager().getName() + " clicked at entity " + interact.getEntity().getName());
        if(interact.getDamager() instanceof Player && interact.getEntity() instanceof Player)
        {
            Player challenger = (Player) interact.getDamager();
            Player challenged = (Player) interact.getEntity();
            List<Challenge> challanges1 = challenges.get(challenger);
            if(challanges1 == null) challanges1 = new List<>();
            Challenge chal = challanges1.find(x -> x.challenger == challenged && x.challenged == challenger);
            if(chal == null)
            {
                SQLPlayer sql = StringStorer.getPlayer(challenger);
                CustomGamemode gm = getGM(challenger);
                String k = "";
                boolean useCustomKit = Boolean.parseBoolean(sql.getOrStoreDefault("Practice_Challenge_CustomKit",
                                                                                  "true"
                ));
                if(useCustomKit)
                {
                    k = challenger.getName();
                }
                else
                {
                    k = gm.defaultKit;
                }
                String m = sql.getOrStoreDefault("Practice_Challenge_CustomMap", "Random");
                if(m.equalsIgnoreCase("Random")) m = gm.maps.random();
                
                Challenge g = new Challenge(challenger, challenged, gm.gamemode, m, k);
                List<Challenge> chals = challenges.get(challenged);
                if(chals == null) chals = new List<>();
                chals.removeAll(x -> x.challenger.getName().equalsIgnoreCase(challenger.getName()));
                chals.add(g);
                challenges.put(challenged, chals);
                new BukkitRunnable(){
                    @Override
                    public void run()
                    {
                        List<Challenge> chals = challenges.get(challenged);
                        if(chals == null) chals = new List<>();
                        chals.remove(g);
                        challenges.put(challenged, chals);
                    }
                }.runTaskLater(Practice.Instance, 20 * 60);
                challenger.sendMessage("§9Challenged " + challenged.getName() + " to a Game of " + gm.item.getDisplayName());
                challenged.sendMessage("§9You have been challenged by " + challenger.getName() + " to a Game of " + gm.item.getDisplayName());
            }
            else{
                NewGamePacket.start(new List(challenged, challenger), chal.gamemode, chal.kit, chal.map, true);
                challenges.remove(challenger);
                challenges.remove(challenged);
            }
        }
    }
    
    private void setInv(BetterInv inv)
    {
        setCustomKit(inv);
        setCustomMap(inv);
        setCustomGm(inv);
    }

    private void setMapInv(BetterInv inv)
    {
        inv.setItem(10, new BetterItem(Material.BARRIER).setDisplayName("§9Random Map"));
        inv.onClickSlot(10, (event) -> {
            inv.clear();
            StringStorer.getPlayer(inv.getPlayer()).storeValue("Practice_Challenge_CustomMap", "Random");
            setInv(inv);
        });
        CustomGamemode g = getGM(inv.getPlayer());
        if(g == null){
            Practice.log(1, "No Gamemode found!");
            return;
        }
        List<String> maps = g.getMaps();
        //set an MAP Item for each map, the Item should be named after the map and it should start at slot 10 and skip 2 slots every 7 slots
        for(int i = 0; i < maps.size(); i++){
            int slot = 11 + i;
            if(slot > 16) slot = slot + 2;
            if(slot > 25) slot = slot + 2;
            if(slot > 34) slot = slot + 2;
            inv.setItem(slot, new BetterItem(Material.MAP).setDisplayName("§b" + maps.get(i)).setLore("§9Click to select this map!"));
            int finalI = i;
            inv.onClickSlot(slot, (event) -> {
                event.setCancelled(true);
                inv.clear();
                StringStorer.getPlayer(inv.getPlayer()).storeValue("Practice_Challenge_CustomMap", maps.get(finalI));
                setInv(inv);
            });
        }
    }

    private void setCustomGMInv(BetterInv inv)
    {
        int slot = 10;
        for(CustomGamemode g: gamemodes)
        {
            Practice.log(4, "Adding Custom Gamemode: " + g.getGamemode());
            inv.setItem(slot, g.getItem());
            inv.onClickSlot(slot, (event) -> {
                event.setCancelled(true);
                inv.clear();
                StringStorer.getPlayer(inv.getPlayer()).storeValue("Practice_Challenge_CustomGamemode", g.getGamemode());
                StringStorer.getPlayer(inv.getPlayer()).storeValue("Practice_Challenge_CustomMap", "Random");
                setInv(inv);
            });
            slot++;
            if(slot == 17) slot = slot + 2;
            if(slot == 26) slot = slot + 2;
            if(slot == 35) slot = slot + 2;
        }
    }

    private void setCustomGm(BetterInv inv)
    {
        inv.setItem(10, new BetterItem(Material.BOOK).setDisplayName("§bSelect Gamemode").setLore("§9You can set the Gamemode \n§9you want to play here"));
        inv.lock(10);
    
        CustomGamemode g = getGM(inv.getPlayer());
        if(g == null){
            Practice.log(1, "No Gamemode found!");
            return;
        }
        inv.setItem(19, g.getItem());
        inv.onClickSlot(19, x ->
        {
            x.setCancelled(true);
            inv.clear();
            setCustomGMInv(inv);
        });
    
        inv.clearOnClose();
        if(g.solo)
        {
            inv.onClose(x -> {
                PracticePlayer pp = PracticePlayer.getPlayer(inv.getPlayer());
                //get kit
                boolean useCustomKit = Boolean.parseBoolean(StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomKit", "true"));
                //get map
                String map = StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomMap", "Random");
                //start game, if not custom kit then use default kit
                NewGamePacket.start(new List(inv.getPlayer()), g.gamemode, useCustomKit ? pp.getKit().getName() : g.defaultKit, map.equalsIgnoreCase("Random") ? g.maps.random() : map, true);
            });
        }
    }
    
    private CustomGamemode getGM(Player p)
    {
        String gm = StringStorer.getPlayer(p).getOrStoreDefault("Practice_Challenge_CustomGamemode", defaultMode);
        CustomGamemode g = gamemodes.find(x -> x.gamemode.equalsIgnoreCase(gm));
        if(g == null) g = gamemodes.find(x -> x.gamemode.equalsIgnoreCase(defaultMode));
        if(g == null) g = gamemodes.first();
        return g;
    }

    private void setCustomMap(BetterInv inv)
    {
        inv.setItem(13, new BetterItem(Material.BOOK).setDisplayName("§bSet a Map").setLore("§9Use the map of your choice"));
        inv.lock(13);

        String customMap = StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomMap", "Random");
        String map = customMap;
        inv.setItem(22, new BetterItem(Material.MAP).setDisplayName("§9Map: §b" + map));
        inv.onClickSlot(22, x ->{
            x.setCancelled(true);
            inv.clear();
            setMapInv(inv);
        });
    }

    private void setCustomKit(BetterInv inv){
        inv.setItem(16, new BetterItem(Material.BOOK).setDisplayName("§bToggle Custom Kit").setLore("§9Use the kit you made in the Kit Editing Area"));
        inv.lock(16);

        boolean customKit = Boolean.parseBoolean(StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomKit", "true"));
        if(customKit) inv.setItem(25, new BetterItem(Material.INK_SACK).NsetDurability((short) 10).setDisplayName("§9Custom Kit: §bEnabled"));
        else inv.setItem(25, new BetterItem(Material.INK_SACK).NsetDurability((short) 8).setDisplayName("§9Custom Kit: §bDisabled"));

        inv.onClickSlot(25, x ->
        {
            x.setCancelled(true);
            inv.clear();
            StringStorer.getPlayer(inv.getPlayer()).storeValue("Practice_Challenge_CustomKit", !customKit + "");
            setInv(inv);
        });
    }
}