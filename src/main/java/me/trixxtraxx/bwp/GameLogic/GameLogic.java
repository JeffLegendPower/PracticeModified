package me.trixxtraxx.bwp.GameLogic;

import me.trixxtraxx.bwp.Gamemode.Game;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class GameLogic
{
    public abstract void start(Game gm, List<Player> players);
    public abstract World getWorld();
}
