package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventItem extends LobbyItem
{
    public EventItem(ConfigurationSection section)
    {
        super(section);
    }
    
    @Override
    public void onClick(PlayerInteractEvent interact)
    {
        interact.setCancelled(true);
        interact.getPlayer().sendMessage("§cComing soon...");
    }
}
