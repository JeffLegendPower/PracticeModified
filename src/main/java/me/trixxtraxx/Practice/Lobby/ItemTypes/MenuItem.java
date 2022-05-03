package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MenuItem extends LobbyItem
{
    String menu;
    public MenuItem(ConfigurationSection section)
    {
        super(section);
        menu = section.getString("Menu");
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
