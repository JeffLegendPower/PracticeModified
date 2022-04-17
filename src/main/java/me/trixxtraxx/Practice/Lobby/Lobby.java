package me.trixxtraxx.Practice.Lobby;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.List;

public class Lobby
{
    private static List<Lobby> lobbies = new ArrayList<>();
    private String world;
    private String name;
    private int maxRatedPlayers;
    private boolean blockBreak;
    private boolean blockPlace;
    private boolean blockDamage;
    private boolean blockEntityDamage;
    private List<String> blockedInventories;
    
    
    public Lobby(ConfigurationSection section)
    {
        this.world = section.getString("Lobby.world");
        this.name = section.getString("Lobby.name");
        this.maxRatedPlayers = section.getInt("Lobby.maxRatedPlayers");
        this.blockBreak = section.getBoolean("Lobby.BlockBreak");
        this.blockPlace = section.getBoolean("Lobby.BlockPlace");
        this.blockDamage = section.getBoolean("Lobby.BlockDamage");
        this.blockEntityDamage = section.getBoolean("Lobby.BlockEntityDamage");
        this.blockedInventories = section.getStringList("Lobby.blockedInventories");
        lobbies.add(this);
    }
    
    public String getWorld(){return world;}
    public String getName(){return name;}
    public int getMaxRatedPlayers(){return maxRatedPlayers;}
    public boolean isBreakBlocked(){return blockBreak;}
    public boolean isPlaceBlocked(){return blockPlace;}
    public boolean isDamageBlocked(){return blockDamage;}
    public boolean isEntityDamageBlocked(){return blockEntityDamage;}
    public List<String> getBlockedInventories(){return blockedInventories;}
    
    
    public static List<Lobby> getLobbies(){return lobbies;}
    public static Lobby get(World world){
        for(Lobby lobby : lobbies){
            if(lobby.getWorld().contentEquals(world.getName()))
            {
                return lobby;
            }
        }
        return null;
    }
}
