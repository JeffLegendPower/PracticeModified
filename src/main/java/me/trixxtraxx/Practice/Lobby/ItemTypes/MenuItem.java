package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

public class MenuItem extends LobbyItem
{
    String menu;
    public MenuItem(Material material, String name, @Nullable String lore, int slot, String menu) {
        super(material, name, lore, slot);
        this.menu = menu;
    }
    
    @Override
    public void onClick(PlayerInteractEvent interact)
    {
        PracticePlayer pp = PracticePlayer.getPlayer(interact.getPlayer());
        pp.openBungeeInventory(menu);
    }
    
    @Override
    public void onClick(EntityDamageByEntityEvent interact)
    {
    
    }
}
