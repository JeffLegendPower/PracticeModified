package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Events.PointGainEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class KillArrowComponent extends GameComponent
{
    public KillArrowComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onPlayerDie(PointGainEvent event)
    {
        Kit k = logic.getGame().getKit();
        List<ItemStack> items = k.getItemStacks();
        ItemStack stack = items.find(x -> x.getType() == Material.ARROW);
        if(stack != null)
        {
            int index = items.indexOf(stack);
            HashMap<Integer, Integer> order = k.getOrder(PracticePlayer.getPlayer(event.getPlayer()));
            int slot = -1;
            if(order != null){
                //slot = order.get(index);
            }
            PlayerInventory inv = event.getPlayer().getInventory();
            if(slot == -1 || inv.getItem(slot) != null) inv.addItem(stack);
            else inv.setItem(slot, stack);
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onProjHit(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Projectile && event.getEntity() instanceof Player)
        {
            event.setDamage(999999999);
        }
    }
}
