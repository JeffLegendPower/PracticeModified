package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SoloSpawnCoponent implements ISpawnComponent
{
    private ConfigLocation loc;

    public SoloSpawnCoponent(Location loc)
    {
        this.loc = new ConfigLocation(loc);
    }

    public SoloSpawnCoponent() {}

    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        return loc.getLocation(logic.getWorld());
    }

    @Override
    public String getData()
    {
        return new Gson().toJson(loc);
    }

    @Override
    public void applyData(String s)
    {
        loc = new Gson().fromJson(s, ConfigLocation.class);
    }
}
