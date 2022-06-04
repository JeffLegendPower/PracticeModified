package me.trixxtraxx.Practice.Bungee;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.IMessageReceived;
import me.trixxtraxx.Practice.GameLogic.Components.Components.InventoryView.InventoryView;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Bukkit;

public class InventoryViewListener implements IMessageReceived
{
    @Override
    public boolean IsChannel(String s)
    {
        return s.equalsIgnoreCase("Practice_InventoryView_View");
    }
    
    @Override
    public void MessageReceived(String s)
    {
        String[] split = s.split("<>");
        PracticePlayer pp = PracticePlayer.getPlayer(Bukkit.getPlayer(split[0]));
        InventoryView view = new Gson().fromJson(split[1], InventoryView.class);
        view.open(pp.getPlayer());
    }
}
