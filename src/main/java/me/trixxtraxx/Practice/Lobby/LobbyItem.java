package me.trixxtraxx.Practice.Lobby;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class LobbyItem
{
    private ItemStack item;
    private int slot;
    
    public LobbyItem(final ItemStack item, final int slot) {
        this.item = item;
        this.slot = slot;
    }

    public LobbyItem(Material material, String name, @Nullable String lore, int slot) {
        BetterItem item = new BetterItem(material);
        item.setDisplayName(name);
        item.setLore(lore != null ? lore : "");
        item.setUnbreakable(true);
        item.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        item.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        this.item = item;
        this.slot = slot;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public abstract void onClick(PlayerInteractEvent interact);
    public abstract void onClick(EntityDamageByEntityEvent interact);
}
