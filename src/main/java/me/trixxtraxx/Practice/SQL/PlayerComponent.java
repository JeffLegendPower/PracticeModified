package me.trixxtraxx.Practice.SQL;

import me.trixxtraxx.Practice.Component;
import org.bukkit.entity.Player;

public abstract class PlayerComponent extends Component
{
    protected Player player;
    public PlayerComponent(Player p){
        player = p;
    }

    public abstract String getData();
}