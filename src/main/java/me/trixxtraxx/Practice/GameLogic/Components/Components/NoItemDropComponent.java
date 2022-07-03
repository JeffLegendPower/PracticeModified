package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class NoItemDropComponent extends GameComponent
{
    public NoItemDropComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onDrop(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }
}
