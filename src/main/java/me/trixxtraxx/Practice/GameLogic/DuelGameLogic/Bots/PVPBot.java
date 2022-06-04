package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Bots;

import me.trixxtraxx.Practice.Practice;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.ai.MCTargetStrategy;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcmonkey.sentinel.SentinelTrait;


public class PVPBot
{
    public static enum Difficulty
    {
        EASY,
        MEDIUM,
        HARD,
        INSANE;
    }
    protected BotGameLogic logic;
    protected Player target;
    protected Location loc;
    protected NPC bot;
    protected Difficulty difficulty;
    
    public PVPBot(BotGameLogic logic, Location loc, Player target)
    {
        this.logic = logic;
        this.target = target;
        bot = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Bot");
        bot.spawn(loc);
        SentinelTrait trait = bot.getOrAddTrait(SentinelTrait.class);
        trait.addTarget("player:" + target.getName());
    }
    
    public void destroy(){
        bot.destroy();
    }
}
