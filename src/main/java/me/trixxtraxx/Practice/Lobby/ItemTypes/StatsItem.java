package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StatsItem extends LobbyItem
{
    public StatsItem(ConfigurationSection section)
    {
        super(section);
    }
    
    @Override
    public void onClick(PlayerInteractEvent interact)
    {
    
    }
}
