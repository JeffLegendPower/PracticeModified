package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SoloSpawnCmponent implements ISpawnComponent
{
    // no location for easy serialisation
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public SoloSpawnCmponent(Location loc)
    {
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        yaw = loc.getYaw();
        pitch = loc.getPitch();
    }

    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        return new Location(logic.getWorld(), x,y,z,yaw,pitch);
    }
}
