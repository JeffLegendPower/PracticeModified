package me.trixxtraxx.Practice.Bungee;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.RestCommunicator.PluginAPI.MessageProvider;

public class StatUpdatePacket
{
    private List<GameAddAction> gameAdd;
    private List<GlobalStatUpdateAction> gameRemove;
    
    public StatUpdatePacket(List<GameAddAction> gameAdd, List<GlobalStatUpdateAction> gameRemove)
    {
        this.gameAdd = gameAdd;
        this.gameRemove = gameRemove;
    }
    
    public void update(){
        for(GameAddAction action : gameAdd){
            action.add();
        }
        for(GlobalStatUpdateAction action : gameRemove){
            action.apply();
        }
    }
    
    public void send()
    {
        String packet = new Gson().toJson(this);
        MessageProvider.SendMessage("Practice_UpdateStats", packet);
    }
}
