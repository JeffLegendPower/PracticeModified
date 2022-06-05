package me.trixxtraxx.Practice.Kit.Components;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Kit.KitComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

public class PearlDelayComponent extends KitComponent
{
    @Config
    public int cooldown;
    
    public PearlDelayComponent(Kit kit)
    {
        super(kit);
        Practice.log(4, "Pearl Delay Component initialized");
    }
    
    private Cache<String, Long> cooldownCache = null;
    
    @TriggerEvent(state = TriggerEvent.CancelState.NONE)
    public void onEnderPearlThrow(PlayerInteractEvent event)
    {
        Practice.log(4, "Pearl Delay Component onEnderPearlThrow");
        if(cooldownCache == null) cooldownCache = CacheBuilder.newBuilder().expireAfterWrite(cooldown, TimeUnit.SECONDS).build();
        Player player = event.getPlayer();
    
        if (player.getItemInHand().getType().equals(Material.ENDER_PEARL)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!cooldownCache.asMap().containsKey(player.getUniqueId())) {
                    cooldownCache.put(player.getName(), System.currentTimeMillis() + cooldown * 1000L);
                }
                else {
                    long remaining = cooldownCache.asMap().get(player.getName()) - System.currentTimeMillis();
                    player.sendMessage("§9You must wait §b" + TimeUnit.MILLISECONDS.toSeconds(remaining) + " s §9to use an ender pearl again");
                    event.setCancelled(true);
                }
            }
        }
    }
}
