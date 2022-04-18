package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomGamemodeItem extends LobbyItem
{
    public CustomGamemodeItem(ConfigurationSection section)
    {
        super(section);
    }
    
    @Override
    public void onClick(PlayerInteractEvent interact)
    {
    
    }
}
