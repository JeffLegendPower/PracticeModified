package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SoloSpawnCoponent implements ISpawnComponent
{
    private SoloSettings settings = new SoloSettings();
    // no location for easy serialisation

    private class SoloSettings
    {
        public double x;
        public double y;
        public double z;
        public float yaw;
        public float pitch;
    }

    public SoloSpawnCoponent(Location loc)
    {
        settings.x = loc.getX();
        settings.y = loc.getY();
        settings.z = loc.getZ();
        settings.yaw = loc.getYaw();
        settings.pitch = loc.getPitch();
    }

    public SoloSpawnCoponent() {}

    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        return new Location(logic.getWorld(), settings.x, settings.y, settings.z, settings.yaw, settings.pitch);
    }

    @Override
    public String getData()
    {
        return new Gson().toJson(settings);
    }

    @Override
    public void applyData(String s)
    {
        settings = new Gson().fromJson(s, SoloSettings.class);
    }
}
