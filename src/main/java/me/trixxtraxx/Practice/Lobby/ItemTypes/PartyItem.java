package me.trixxtraxx.Practice.Lobby.ItemTypes;

import me.trixxtraxx.Practice.Lobby.LobbyItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

public class PartyItem extends LobbyItem
{
    
    public PartyItem(Material material, String name, @Nullable String lore, int slot) {
        super(material, name, lore, slot);
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
