package me.trixxtraxx.Practice.Bungee;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.IMessageReceived;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BungeeListener implements IMessageReceived, Listener
{
    
    @Override
    public boolean IsChannel(String s)
    {
        if(s.equalsIgnoreCase("Practice_NewGame")) return true;
        return false;
    }
    
    @Override
    public void MessageReceived(String s)
    {
        NewGamePacket packet = new Gson().fromJson(s, NewGamePacket.class);
        packet.init();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event)
    {
        NewGamePacket packet = NewGamePacket.get(event.getPlayer());
        if(packet != null)
        {
            packet.update();
        }
        else
        {
            Practice.log(4, "Sending " + event.getPlayer().getName() + " to the lobby");
            BungeeUtil.getInstance().toLobby(event.getPlayer());
        }
    }
}