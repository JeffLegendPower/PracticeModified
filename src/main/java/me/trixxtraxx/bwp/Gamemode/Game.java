package me.trixxtraxx.bwp.Gamemode;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.Kit.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private static List<Game> games = new ArrayList<>();
    private GameLogic logic;
    private Kit kit;


    public Game(GameLogic log, List<Player> players, Kit k)
    {
        kit = k;
        logic = log;
        logic.start(this, players);
    }

    public GameLogic getLogic(){return logic;}
    public Kit getKit(){return kit;}
}