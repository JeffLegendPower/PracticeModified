package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Bots;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BotGameLogic extends DuelGameLogic
{
    protected PVPBot bot;
    
    @Override
    public void start(Game gm, List<Player> players, Map m)
    {
        if(players.size()!= 1)
        {
            for(Player p:players)
            {
                p.sendMessage("§cSorry but something went very wrong!");
            }
            Practice.log(1, "Tried to start Bot logic with " + players.size() + " players!");
            game.stop(false);
            return;
        }
        map = m;
        game = gm;
        p1 = players.get(0);
        
        loadWorld();
        BotGameLogic log = this;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                everyoneToSpawn();
                bot = new PVPBot(log, map.getSpawn().getSpawn(log, null), p1);
                triggerEvent(new StartEvent(log));
                addComponent(new BotComponent(log));
            }
        }.runTaskLater(Practice.Instance, 0);
    }
    
    @Override
    public void stop(boolean dc)
    {
        if(triggerEvent(new StopEvent(this, dc)).isCanceled()) return;
        Practice.log(3, "Stopping duel game");
        game.stop(false);
        bot.destroy();
    }
    
    @Override
    public List<Player> getPlayers()
    {
        return new List<>(p1);
    }
    
    @Override
    public Player getP2() {return null;}
    
    public PVPBot getBot() {return bot;}
}
