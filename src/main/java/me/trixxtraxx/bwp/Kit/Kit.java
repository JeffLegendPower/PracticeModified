package me.trixxtraxx.bwp.Kit;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.PlayerInventory;

public class Kit
{
    public void setInventory(Player p)
    {
        clearInventory(p);
    }

    private void clearInventory(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        if(p.getOpenInventory() != null && p.getOpenInventory().getType() == InventoryType.PLAYER) p.closeInventory();
    }
}