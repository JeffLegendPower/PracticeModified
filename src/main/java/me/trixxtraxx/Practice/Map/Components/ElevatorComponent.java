package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ElevatorComponent extends MapComponent
{
    @Config
    private Material mat;
    
    public ElevatorComponent(Map map, Material mat)
    {
        super(map);
        this.mat = mat;
    }
    public ElevatorComponent(Map map)
    {
        super(map);
    }
    
    @TriggerEvent
    public void onJump(PlayerMoveEvent event){
        if(event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if(event.getFrom().getY() < event.getTo().getY()) {
            if(event.getPlayer().getLocation().clone().add(0, -0.5,0).getBlock().getType() == mat) {
                //search for the next block above the player and tp the player that many blocks up
                int y = event.getFrom().getBlockY();
                while(event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation().getBlockX(), y, event.getPlayer().getLocation().getBlockZ()).getType() != mat)
                {
                    y++;
                    if(y > 320) return;
                }
                event.getTo().setY(y + 1);
                //make wosch sound
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BAT_TAKEOFF, 1, 1);
            }
        }
    }
    
    @TriggerEvent
    public void onSneak(PlayerToggleSneakEvent event){
        if(event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if(event.isSneaking()) {
            if(event.getPlayer().getLocation().clone().add(0, -0.5,0).getBlock().getType() == mat) {
                //search the next block below the player and tp the player that many blocks down
                int y = event.getPlayer().getLocation().getBlockY() - 2;
                while(event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation().getBlockX(), y, event.getPlayer().getLocation().getBlockZ()).getType() != mat)
                {
                    y--;
                    if(y < -64) return;
                }
                Location newLoc = event.getPlayer().getLocation();
                newLoc.setY(y + 1);
                event.getPlayer().teleport(newLoc);
                //make wosch sound
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BAT_TAKEOFF, 1, 1);
            }
        }
    }
}
