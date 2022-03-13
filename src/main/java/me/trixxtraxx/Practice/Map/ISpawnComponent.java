package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ISpawnComponent
{
    public Location getSpawn(GameLogic logic, Player p);
    public String getData();
    public void applyData(String s);
}
