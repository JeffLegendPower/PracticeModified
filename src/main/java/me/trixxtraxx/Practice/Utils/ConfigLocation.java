package me.trixxtraxx.Practice.Utils;

import org.bukkit.Location;
import org.bukkit.World;

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
    public double getYaw(){return yaw;}
    public double getPitch(){return pitch;}

    public Location getLocation(World w) {return new Location(w, x,y,z,yaw,pitch);}
}
