package me.trixxtraxx.Practice.Bungee;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.entity.Player;

public class ServerUpdatePacket
{
    private boolean online;
    private String name;
    private int maxRatedPlayers;
    private List<Game> games = new List();
    private List<Lobby> lobbies = new List();
    
    private class Lobby{
        private List<String> players = new List();
        private String name;
    }
    
    private class Game{
        private List<String> players = new List();
        private String gamemode;
        private String kit;
        private String map;
    }
    
    public ServerUpdatePacket(String name, int maxRatedPlayers, List<me.trixxtraxx.Practice.Gamemode.Game> games, List<me.trixxtraxx.Practice.Lobby.Lobby> lobbies)
    {
        this.online = true;
        this.name = name;
        this.maxRatedPlayers = maxRatedPlayers;
        for(me.trixxtraxx.Practice.Gamemode.Game game : games)
        {
            Game g = new Game();
            for(Player p: game.getLogic().getPlayers())
            {
                g.players.add(p.getName());
            }
            g.gamemode = game.getLogic().getName();
            g.kit = game.getKit().getName();
            g.map = game.getLogic().getMap().getName();
            this.games.add(g);
        }
        
        for(me.trixxtraxx.Practice.Lobby.Lobby lobby : lobbies)
        {
            Lobby l = new Lobby();
            for(PracticePlayer p: lobby.getPlayers())
            {
                l.players.add(p.getPlayer().getName());
            }
            l.name = lobby.getName();
            this.lobbies.add(l);
        }
    }
    
    public ServerUpdatePacket(String name, boolean online) {
        this.name = name;
        this.online = online;
    }
}
