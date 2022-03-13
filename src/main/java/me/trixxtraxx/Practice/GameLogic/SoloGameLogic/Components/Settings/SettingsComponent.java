package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsComponent extends GameComponent implements ISettingsComponent
{
    private Material mat;
    private List<StoredInventory> inv = new ArrayList<>();
    private String title = ChatColor.AQUA + "Settings";

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    private class StoredInventory
    {
        Player p;
        List<ISettingsComponent> comps;
    }

    public SettingsComponent(GameLogic logic, Material open)
    {
        super(logic);
        this.mat = open;
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerInteractEvent) onInteract((PlayerInteractEvent) event);
    }

    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(e.getItem() != null && e.getItem().getType() == mat)
        {
            Inventory setting = Bukkit.createInventory(p,53,title);
            p.openInventory(setting);
        }
    }
}

