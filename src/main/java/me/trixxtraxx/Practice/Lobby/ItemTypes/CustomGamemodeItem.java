package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.StringStorer.StringStorer;
import me.trixxtraxx.Practice.Lobby.LobbyItem;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomGamemodeItem extends LobbyItem
{
    public class CustomGamemode
    {
        BetterItem item;
        String gamemode;
        List<String> maps;

        public CustomGamemode(BetterItem item, String gamemode, List<String> maps)
        {
            this.item = item;
            this.gamemode = gamemode;
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
    }

    List<CustomGamemode> gamemodes = new List();
    String defaultMode;
    public CustomGamemodeItem(ConfigurationSection section)
    {
        super(section);
        defaultMode = section.getString("DefaultGamemode");
        ConfigurationSection gms = section.getConfigurationSection("Gamemodes");
        for (String key: gms.getKeys(false)){
            ConfigurationSection g = gms.getConfigurationSection(key);
            BetterItem i = new BetterItem(g.getString("Material"));
            if(g.contains("Name")) i.setDisplayName(g.getString("Name"));
            if(g.contains("Lore")) i.setLore(g.getStringList("Lore"));
            gamemodes.add(new CustomGamemode(
                    i, key, new List(g.getStringList("Maps"))
            ));
        }
    }

    @Override
    public void onClick(PlayerInteractEvent interact)
    {
        Player p = interact.getPlayer();
        BetterInv inv = new BetterInv(p, 54, "Custom Gamemode");
        setInv(inv);
    }

    private void setInv(BetterInv inv)
    {
        setCustomKit(inv);
        setCustomMap(inv);
    }

    private void setMapInv(BetterInv inv)
    {
        //TODO: show default maps
    }

    private void setCustomGM(BetterInv inv){
        //TODO: show gamemodes specified
    }

    private void setCustomGm(BetterInv inv)
    {
        inv.setItem(18, new BetterItem(Material.BOOK).setDisplayName("§bToggle Custom Gamemode").setLore("§9Use the gamemode you made in the Gamemode Editing Area"));
        inv.lock(18);

        String gm = StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomGamemode", defaultMode);
        CustomGamemode g = gamemodes.find(x -> x.gamemode.equalsIgnoreCase(gm);
        if(g == null) g = gamemodes.find(x -> x.gamemode.equalsIgnoreCase(defaultMode));
        if(g == null) g = gamemodes.first();
        if(g == null){
            Practice.log(1, "No Gamemode found!");
            return;
        }
        inv.setItem(27, g.getItem());
        inv.onClickSlot(27, x ->
        {
            inv.clear();
            setCustomGM(inv);
        });
    }

    private void setCustomMap(BetterInv inv)
    {
        inv.setItem(14, new BetterItem(Material.BOOK).setDisplayName("§bSet a Map").setLore("§9Use the map of your choice"));
        inv.lock(14);

        String customMap = StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomMap", "Random");
        String map = customMap;
        inv.setItem(23, new BetterItem(Material.MAP).setDisplayName("§9Map: §b" + map));
        inv.onClickSlot(23, x ->{
            inv.clear();
            setMapInv(inv);
        });
    }

    private void setCustomKit(BetterInv inv){
        inv.setItem(16, new BetterItem(Material.BOOK).setDisplayName("§bToggle Custom Kit").setLore("§9Use the kit you made in the Kit Editing Area"));
        inv.lock(16);

        boolean customKit = Boolean.parseBoolean(StringStorer.getPlayer(inv.getPlayer()).getOrStoreDefault("Practice_Challenge_CustomKit", "true"));
        if(customKit) inv.setItem(25, new BetterItem(Material.INK_SACK).NsetDurability((short) 10));
        else inv.setItem(25, new BetterItem(Material.INK_SACK).NsetDurability((short) 8));

        inv.onClickSlot(25, x ->
        {
            inv.clear();
            StringStorer.getPlayer(inv.getPlayer()).storeValue("Practice_Challenge_CustomKit", !customKit + "");
            setInv(inv);
        });
    }
}