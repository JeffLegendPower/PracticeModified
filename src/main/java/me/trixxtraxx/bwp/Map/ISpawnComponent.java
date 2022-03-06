package me.trixxtraxx.bwp.Map;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ISpawnComponent
{
    public Location getSpawn(GameLogic logic, Player p);
}
