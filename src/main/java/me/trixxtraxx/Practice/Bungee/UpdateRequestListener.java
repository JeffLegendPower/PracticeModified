package me.trixxtraxx.Practice.Bungee;

import me.TrixxTraxx.RestCommunicator.PluginAPI.IMessageReceived;

public class UpdateRequestListener implements IMessageReceived

{
    @Override
    public boolean IsChannel(String s)
    {
        return s.equalsIgnoreCase("Practice_SendStatus");
    }
    
    @Override
    public void MessageReceived(String s)
    {
        BungeeUtil.getInstance().update();
    }
}
