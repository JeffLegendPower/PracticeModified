package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class SettingsComponent extends GameComponent
{
    private Material mat;
    private List<StoredInventory> inv = new ArrayList<>();

    private class StoredInventory
    {
        Player p;
        List<ISettingsComponent> comps;
    }

    public SettingsComponent(GameLogic logic, Material open)
    {
        super(logic);
        mat = open;
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerInteractEvent) onInteract((PlayerInteractEvent) event);
    }

    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getItem() != null && e.getItem().getType() == mat)
        {
            //open gui
        }
    }
}

