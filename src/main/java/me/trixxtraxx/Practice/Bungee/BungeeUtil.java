package me.trixxtraxx.Practice.Bungee;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.MessageProvider;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Practice;

public class BungeeUtil
{
    private static BungeeUtil instance;
    private boolean isConnected = false;
    private String name;
    private int maxRatedPlayers;
    
    private BungeeUtil() {}
    
    public static BungeeUtil getInstance(){
        if (instance == null)
        {
            instance = new BungeeUtil();
        }
        return instance;
    }
    
    public void init(String bungee, int maxRatedPlayerCount)
    {
        this.isConnected = true;
        this.name = bungee;
        this.maxRatedPlayers = maxRatedPlayerCount;
        update();
    }
    
    public void update(){
        if(!this.isConnected) return;
        
        ServerUpdatePacket packet = new ServerUpdatePacket(name, maxRatedPlayers, Game.getGames(), Lobby.getLobbies());
        Practice.log(4, "Updating status for " + Game.getGames().size() + " games and " + Lobby.getLobbies().size() + " lobbies");
        
        MessageProvider.SendMessage("PracticeServerUpdate", new Gson().toJson(packet));
    }
}
