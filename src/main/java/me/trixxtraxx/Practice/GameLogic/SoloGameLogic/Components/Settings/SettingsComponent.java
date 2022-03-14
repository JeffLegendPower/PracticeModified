package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Components.SpawnProtComponent;
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
    private  Settings settings = new Settings();
    private List<StoredInventory> inv = new ArrayList<>();
    private class Settings
    {
        private Material mat;
        private String title = ChatColor.AQUA + "Settings";
    }


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

    public SettingsComponent(GameLogic logic, Material open, String title)
    {
        super(logic);
        settings.mat = open;
        settings.title = title;
    }
    public SettingsComponent(GameLogic logic, String s)
{
    super(logic);
    settings = new Gson().fromJson(s, Settings.class);
}
    @Override
    public String getData() {return new Gson().toJson(settings);}

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerInteractEvent) onInteract((PlayerInteractEvent) event);
    }

    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(e.getItem() != null && e.getItem().getType() == settings.mat)
        {
            Inventory setting = Bukkit.createInventory(p,53,settings.title);
            p.openInventory(setting);
        }
    }
}

