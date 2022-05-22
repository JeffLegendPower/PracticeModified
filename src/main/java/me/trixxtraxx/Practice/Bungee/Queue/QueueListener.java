package me.trixxtraxx.Practice.Bungee.Queue;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.IMessageReceived;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Bukkit;

public class QueueListener implements IMessageReceived
{
    @Override
    public boolean IsChannel(String s)
    {
        return s.equalsIgnoreCase("Practice_Queue_Update_Spigot");
    }
    
    @Override
    public void MessageReceived(String s)
    {
        QueueUpdatePacket packet = new Gson().fromJson(s, QueueUpdatePacket.class);
        PracticePlayer pp = PracticePlayer.getPlayer(Bukkit.getPlayer(packet.getPlayer()));
        pp.setInQueue(packet.isInQueue());
    }
}
