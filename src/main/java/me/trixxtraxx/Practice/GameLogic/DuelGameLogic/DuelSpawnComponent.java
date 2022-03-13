package me.trixxtraxx.Practice.GameLogic.DuelGameLogic;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloSpawnCoponent;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

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