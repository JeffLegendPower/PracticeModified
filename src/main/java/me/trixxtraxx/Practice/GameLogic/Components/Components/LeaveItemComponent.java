package me.trixxtraxx.Practice.GameLogic.Components.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeaveItemComponent extends GameComponent
{
    private Material mat;
    public LeaveItemComponent(GameLogic logic, Material mat)
    {
        super(logic);
        this.mat = mat;
    }
    public LeaveItemComponent(GameLogic logic, String s)
    {
        super(logic);
        mat = new Gson().fromJson(s, Material.class);
    }
    @Override
    public String getData() {return new Gson().toJson(mat);}

    @Override
    public void onEvent(Event event){
        if(event instanceof PlayerInteractEvent) onInteract((PlayerInteractEvent) event);
    }

    public void onInteract(PlayerInteractEvent e)
    {
        if(e.getItem() != null && e.getItem().getType() == mat)
        {
            e.setCancelled(true);
            e.getPlayer().performCommand("lobby");
        }
    }
}