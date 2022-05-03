package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PartyItem extends LobbyItem
{
    
    public PartyItem(ConfigurationSection section)
    {
        super(section);
    }
    
    @Override
    public void onClick(PlayerInteractEvent interact)
    {
        interact.setCancelled(true);
        interact.getPlayer().sendMessage("§cComing soon...");
    }
    
    @Override
    public void onClick(EntityDamageByEntityEvent interact)
    {
    
    }
}
