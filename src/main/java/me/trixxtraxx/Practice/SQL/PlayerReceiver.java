package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.IMessageReceived;
import me.trixxtraxx.Practice.Practice;

public class PlayerReceiver implements IMessageReceived
{
    
    @Override
    public boolean IsChannel(String s)
    {
        if(s.equalsIgnoreCase("Player_Update")) return true;
        return false;
    }
    
    @Override
    public void MessageReceived(String s)
    {
        Practice.log(4, "Player Data received");
        //deserialize to PracticePlayer using Gson
        PracticePlayer player = new Gson().fromJson(s, PracticePlayer.class);
        //add to cache
        CacheListener.add(player);
    }
}
