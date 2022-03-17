package me.trixxtraxx.Practice.SQL;

import org.bukkit.entity.Player;

public abstract class PlayerComponent
{
    protected Player player;
    public PlayerComponent(Player p){
        player = p;
    }

    public abstract String getData();
}