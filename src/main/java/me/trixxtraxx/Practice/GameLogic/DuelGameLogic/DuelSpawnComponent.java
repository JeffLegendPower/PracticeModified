package me.trixxtraxx.Practice.GameLogic.DuelGameLogic;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloSpawnCoponent;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import me.TrixxTraxx.Linq.List;

public class DuelSpawnComponent implements ISpawnComponent
{
    private DuelSettings[] settings;

    private class DuelSettings
    {
        public double x;
        public double y;
        public double z;
        public float yaw;
        public float pitch;
    }
    
    public DuelSpawnComponent(){}
    
    public DuelSpawnComponent(double x1, double y1, double z1, float yaw1, float pitch1, double x2, double y2, double z2, float yaw2, float pitch2)
    {
        settings = new DuelSettings[2];
        settings[0] = new DuelSettings();
        settings[0].x = x1;
        settings[0].y = y1;
        settings[0].z = z1;
        settings[0].yaw = yaw1;
        settings[0].pitch = pitch1;
        settings[1] = new DuelSettings();
        settings[1].x = x2;
        settings[1].y = y2;
        settings[1].z = z2;
        settings[1].yaw = yaw2;
        settings[1].pitch = pitch2;
    }

    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        DuelSettings set = settings[0];
        if(logic instanceof  DuelGameLogic)
        {
            DuelGameLogic log = (DuelGameLogic) logic;
            if(log.getP2() == p) set = settings[1];
        }
        return new Location(logic.getWorld(), set.x,set.y,set.z,set.yaw,set.pitch);
    }

    @Override
    public String getData()
    {
        return new Gson().toJson(settings);
    }

    @Override
    public void applyData(String s)
    {
        settings = new Gson().fromJson(s, DuelSettings[].class);
    }
}