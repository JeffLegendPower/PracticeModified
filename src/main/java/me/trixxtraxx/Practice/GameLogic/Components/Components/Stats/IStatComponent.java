package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

import java.util.HashMap;

public interface IStatComponent
{
    public String getStat(Player p);
    public String getSQLName();
    public String getSQLType();
}