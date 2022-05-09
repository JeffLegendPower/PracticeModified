package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.AllModes.SpectatorRespawnEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SpectatorRespawnComponent extends GameComponent
{
    @Config
    protected int spawnProt;
    @Config
    protected String remainingTitle;
    @Config
    protected String remainingSubtitle;
    @Config
    protected String startingTitle;
    @Config
    protected String startingSubtitle;
    @Config
    protected boolean onStart;
    
    protected boolean started = false;
    protected HashMap<Player, Location> prot = new HashMap<>();
    
    public SpectatorRespawnComponent(GameLogic logic, int spawnProt, String remainingTitle, String remainingSubtitle, String startingTitle, String startingSubtitle, boolean onStart)
    {
        super(logic);
        this.spawnProt = spawnProt;
        this.remainingTitle = remainingTitle;
        this.remainingSubtitle = remainingSubtitle;
        this.startingTitle = startingTitle;
        this.startingSubtitle = startingSubtitle;
        this.onStart = onStart;
        started = onStart;
    }
    
    public SpectatorRespawnComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1)
    public void onEvent(ToSpawnEvent e)
    {
        Practice.log(4, "SpectatorRespawnComponent: " + e.getPlayer().getName() + " 1");
        if(prot.get(e.getPlayer()) != null || !started) return;
        Practice.log(4, "SpectatorRespawnComponent: " + e.getPlayer().getName() + " 2");
        if(logic.triggerEvent(new SpectatorRespawnEvent(logic, e.getPlayer())).isCanceled()) return;
        Practice.log(4, "SpectatorRespawnComponent: " + e.getPlayer().getName() + " 3");
        e.setCanceled(true);
        prot.put(e.getPlayer(), e.getLoc());
        e.getPlayer().setGameMode(org.bukkit.GameMode.SPECTATOR);
        new BukkitRunnable()
        {
            int left = spawnProt;
            @Override
            public void run()
            {
                if(logic.getGame().hasEnded()) {
                    cancel();
                    return;
                }
                if(left == 0)
                {
                    e.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                    e.getPlayer().sendTitle(
                            logic.applyPlaceholders(e.getPlayer(), startingTitle.replace("{Timer}", (left / 20) + "")),
                            logic.applyPlaceholders(e.getPlayer(), startingSubtitle.replace("{Timer}", (left / 20) + ""))
                    );
                    logic.toSpawn(e.getPlayer());
                    prot.remove(e.getPlayer());
                    cancel();
                    return;
                }
                if(left % 20 == 0)
                {
                    e.getPlayer().sendTitle(
                            logic.applyPlaceholders(e.getPlayer(), remainingTitle.replace("{Timer}", (left / 20) + "")),
                            logic.applyPlaceholders(e.getPlayer(), remainingSubtitle.replace("{Timer}", (left / 20) + ""))
                            );
                }
                left--;
            }
        }.runTaskTimer(Practice.Instance, 0,1);
    }
    
    @TriggerEvent
    public void onStart(StartEvent e){
        started = true;
    }
}
