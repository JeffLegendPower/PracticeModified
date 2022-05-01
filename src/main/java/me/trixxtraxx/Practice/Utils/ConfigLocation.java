package me.trixxtraxx.Practice.Utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConfigLocation
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public ConfigLocation(double x,double y,double z,float yaw,float pitch)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ConfigLocation(double x,double y,double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        yaw = 0;
        pitch = 90;
    }

    public ConfigLocation(Location loc)
    {
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        yaw = loc.getYaw();
        pitch = loc.getPitch();
    }

    public double getX(){return x;}
    public double getY(){return y;}
    public double getZ(){return z;}
    public float getYaw(){return yaw;}
    public float getPitch(){return pitch;}

    public Location getLocation(World w) {return new Location(w, x,y,z,yaw,pitch);}
    
    public String serialize()
    {
        return x + "," + y + "," + z + "," + yaw + "," + pitch;
    }
    
    public static ConfigLocation deserialize(String s)
    {
        String[] args = s.split(",");
        return new ConfigLocation(Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2]),Float.parseFloat(args[3]),Float.parseFloat(args[4]));
    }
    
    @Override
    public String toString(){
        return serialize();
    }
}
