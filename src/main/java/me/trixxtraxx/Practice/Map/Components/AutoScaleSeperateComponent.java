package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloAutoscaleLogic;
import me.trixxtraxx.Practice.Map.Components.AutoScaleComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class AutoScaleSeperateComponent extends MapComponent
{
    private SoloAutoscaleLogic logic;
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
    
    public AutoScaleSeperateComponent(Map map)
    {
        super(map);
    }
    
    @TriggerEvent
    public void onStart(StartEvent e)
    {
        logic = (SoloAutoscaleLogic) e.getlogic();
        
        scale = (AutoScaleComponent) map.getComponents(AutoScaleComponent.class).get(0);
    
        Practice.log(4, "AutoScaleSeperateComponent: " + scale.xOffset + " " + scale.yOffset + " " + scale.zOffset);
        
        Location mySpawn = map.getSpawn().getSpawn(e.getlogic(), e.getlogic().getPlayers().first());
        
        x = mySpawn.getX();
        y = mySpawn.getY();
        z = mySpawn.getZ();
        
        Location leftSpawn = new Location(mySpawn.getWorld(), mySpawn.getX() - scale.xOffset, mySpawn.getY() - scale.yOffset, mySpawn.getZ() - scale.zOffset);
        
        x1 = leftSpawn.getX();
        y1 = leftSpawn.getY();
        z1 = leftSpawn.getZ();
        
        Location rightSpawn = new Location(mySpawn.getWorld(), mySpawn.getX() + scale.xOffset, mySpawn.getY() + scale.yOffset, mySpawn.getZ() + scale.zOffset);
        
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
            logic.reset(false);
        }
    }
}
