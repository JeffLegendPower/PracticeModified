package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings.SettingsComponent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class DropItemComponent extends GameComponent
{
    private Settings settings = new Settings();
    private class Settings{
        private Material drop;
        private List<Material> removeItems;
    }

    public DropItemComponent(GameLogic logic, Material drop, List<Material> removeItems)
    {
        super(logic);
        settings.drop = drop;
        settings.removeItems = removeItems;
    }
    public DropItemComponent(GameLogic logic){super(logic);}
    
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    @SuppressWarnings("deprecation")
    public void onDrop(PlayerInteractEvent e)
    {
        if(e.getItem() != null && e.getItem().getType() == settings.drop)
        {
            if(logic.triggerEvent(new DropEvent(logic)).isCanceled()) return;
            e.setCancelled(true);

            PlayerInventory inv = e.getPlayer().getInventory();
            for (Material mat:settings.removeItems) inv.remove(mat);
        }
    }
}