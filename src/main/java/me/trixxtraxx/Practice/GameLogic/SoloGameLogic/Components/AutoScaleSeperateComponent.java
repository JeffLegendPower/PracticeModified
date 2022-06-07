package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloAutoscaleLogic;
import me.trixxtraxx.Practice.Map.Components.AutoScaleComponent;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class AutoScaleSeperateComponent extends GameComponent
{
    private SoloAutoscaleLogic log;
    private AutoScaleComponent scale;
    private double x;
    private double y;
    private double z;
    
    private double x1;
    private double y1;
    private double z1;
    
    private double x2;
    private double y2;
    private double z2;
    
    public AutoScaleSeperateComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onStart(StartEvent e)
    {
        log = (SoloAutoscaleLogic) logic;
        
        scale = (AutoScaleComponent) log.getMap().getComponents(AutoScaleComponent.class).get(0);
    
        Location mySpawn = log.getSpawn();
        
        x = mySpawn.getX();
        y = mySpawn.getY();
        z = mySpawn.getZ();
        
        Location leftSpawn = new Location(mySpawn.getWorld(), mySpawn.getX() - scale.xOffset, mySpawn.getY() - scale.yOffset, mySpawn.getZ() - scale.zOffset);
        
        x1 = leftSpawn.getX();
        y1 = leftSpawn.getY();
        z1 = leftSpawn.getZ();
        
        Location rightSpawn = new Location(mySpawn.getWorld(), mySpawn.getX() + scale.xOffset, mySpawn.getY() - scale.yOffset, mySpawn.getZ() - scale.zOffset);
        
        x2 = rightSpawn.getX();
        y2 = rightSpawn.getY();
        z2 = rightSpawn.getZ();
    }
    
    @TriggerEvent
    public void onMove(PlayerMoveEvent event)
    {
        Location playerLoc = event.getTo();
        
        //calculate the distance to x/y/z and x1/y1/z1 and x2/y2/z2 and then kill the player if x1/y1/z1 or x2/y2/z2 is closer then x/y/z
        
        Location loc = new Location(playerLoc.getWorld(), x, y, z);
        Location loc1 = new Location(playerLoc.getWorld(), x1, y1, z1);
        Location loc2 = new Location(playerLoc.getWorld(), x2, y2, z2);
        
        double distance = playerLoc.distance(loc);
        double distance1 = playerLoc.distance(loc1);
        double distance2 = playerLoc.distance(loc2);
        
        if(distance2 < distance || distance1 < distance)
        {
            log.reset(false);
        }
    }
}
