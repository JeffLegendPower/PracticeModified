package me.trixxtraxx.bwp.GameLogic.SoloGameLogic;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.Map.ISpawnComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnComponentSolo implements ISpawnComponent
{
    // no location for easy serialisation
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        return new Location(logic.getWorld(), x,y,z,yaw,pitch);
    }
}
