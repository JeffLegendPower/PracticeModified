package me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.AllModes.SpectatorRespawnEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.SpectatorRespawnComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Events.PointGainEvent;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.FFALogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.Components.InvisPracticeSpawnProvider;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import me.trixxtraxx.Practice.TriggerEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InvisPracticeComponent extends GameComponent implements IStatComponent
{
    @Config
    private int maxPoints;
    @Config
    private int seconds;
    @Config
    private String atkKit;
    
    protected HashMap<Player, Integer> pointMap = new HashMap<>();
    protected int initialPlayers = 0;
    protected Player currentPlayer;
    protected int timeLeft = 0;
    protected Kit kit;
    protected BukkitRunnable Timer;
    protected boolean cancelSpecRespawn = false;
    
    public InvisPracticeComponent(GameLogic logic, int maxPoints, int seconds, String atkKit)
    {
        super(logic);
        this.maxPoints = maxPoints;
        this.seconds = seconds;
        this.atkKit = atkKit;
    }
    public InvisPracticeComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onStart(StartEvent event)
    {
        initialPlayers = logic.getPlayers().size();
        //run async
        new BukkitRunnable(){
            @Override
            public void run()
            {
                kit = SQLUtil.Instance.getKit(atkKit);
                SQLUtil.Instance.applyComponents(kit);
                startRound();
            }
        }.runTaskAsynchronously(Practice.Instance);
    }
    
    protected void startRound()
    {
        logic.triggerEvent(new ResetEvent(logic, false));
        List<GameComponent> specComps = logic.getComponents(SpectatorRespawnComponent.class);
        for(GameComponent comp : specComps)
        {
            SpectatorRespawnComponent specComp = (SpectatorRespawnComponent) comp;
            for(BukkitRunnable run : specComp.respawns){
                run.cancel();
            }
            specComp.respawns.clear();
        }
        if(currentPlayer == null) currentPlayer = logic.getPlayers().get(0);
        else{
            int index = logic.getPlayers().indexOf(currentPlayer);
            if(index == logic.getPlayers().size() - 1) index = 0;
            else index++;
            currentPlayer = logic.getPlayers().get(index);
        }
        timeLeft = seconds;
        if(Timer != null) Timer.cancel();
        Timer = new BukkitRunnable(){
    
            @Override
            public void run()
            {
                timeLeft--;
                if(timeLeft == 0)
                {
                    if(!logic.getGame().hasEnded()) reset();
                    cancel();
                }
            }
        };
        Timer.runTaskTimer(Practice.Instance, 20, 20);
        
        cancelSpecRespawn = true;
        
        for(Player player : logic.getPlayers())
        {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getMaxHealth());
            //clear potion effects
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            if(player == currentPlayer) continue;
            Practice.log(4, "Sending " + player.getName() + " to spawn");
            logic.toSpawn(player);
        }
        
        InvisPracticeSpawnProvider spawnProvider = (InvisPracticeSpawnProvider) logic.getMap().getComponents(InvisPracticeSpawnProvider.class).get(0);
        Location loc = spawnProvider.getLocation();
        if(loc == null)
        {
            Practice.log(1, "No spawn location found for InvisPracticeComponent");
            currentPlayer.teleport(new Location(logic.getWorld(), 0, 100, 0, 180, 0));
        }
        else
        {
            currentPlayer.teleport(loc);
        }
        
        cancelSpecRespawn = false;
        
        kit.setInventory(currentPlayer, true);
    }
    
    private void reset(){
        logic.broadcast(ChatColor.BLUE + currentPlayer.getName() + " has failed!");
        startRound();
    }
    
    @TriggerEvent
    public void onSpecRespawn(SpectatorRespawnEvent event)
    {
        if(cancelSpecRespawn) event.setCanceled(true);
    }
    
    @TriggerEvent
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if(event.getEntity() == currentPlayer)
        {
            cancelSpecRespawn = true;
            new BukkitRunnable(){
                @Override
                public void run()
                {
                    reset();
                }
            }.runTaskLater(Practice.Instance, 0);
        }
    }
    
    @TriggerEvent(priority = 2)
    public void onDie(PlayerDeathEvent event)
    {
        if(!cancelSpecRespawn) logic.toSpawn(event.getEntity());
    }
    
    @TriggerEvent
    public void onBedBreak(BlockBreakEvent event)
    {
        if(event.getBlock().getType() == Material.BED || event.getBlock().getType() == Material.BED_BLOCK){
            event.setCancelled(true);
            if(event.getPlayer() == currentPlayer)
            {
                logic.broadcast(ChatColor.BLUE + currentPlayer.getName() + " has succeeded!");
    
                Object p = pointMap.get(currentPlayer);
                int points = 0;
                if(p != null)
                {
                    points = (int) p;
                }
                points++;
                if(logic.triggerEvent(new PointGainEvent(logic, currentPlayer, points)).isCanceled()) return;
                pointMap.put(currentPlayer, points);
                
                if(points == maxPoints)
                {
                    logic.stop(false);
                    return;
                }
                
                startRound();
            }
        }
    }
    
    @Override
    public String applyPlaceholder(Player p, String s)
    {
        if(logic.getPlayers().size() > initialPlayers) initialPlayers = logic.getPlayers().size();
        String newS = s;
        int index = 1;
        for(Map.Entry<Player, Integer> entry : pointMap.entrySet())
        {
            newS = newS
                    .replace("{Points" + index + "}", String.valueOf(entry.getValue()))
                    .replace("{Points" + index + "Player}", entry.getKey().getName());
            index++;
        }
        //replace the placeholders above with "" if they dont exist
        for(int i = pointMap.size() + 1; i < initialPlayers + 1; i++)
        {
            newS = newS
                    .replace("{Points" + i + "}", "0")
                    .replace("{Points" + i + "Player}", "");
        }
        //add {Place}, {Name}, {Points}
        //sort pointmap by points
        List<Map.Entry<Player, Integer>> list = new List();
        for(Map.Entry<Player, Integer> entry : pointMap.entrySet())
        {
            list.add(entry);
        }
        Collections.sort(list, (a, b) -> b.getValue().compareTo(a.getValue()));
        //init String place from the index of Player p in pointMap
        String place = "";
        //set place to x if the player is not in the list
        if(!pointMap.containsKey(p)) place = "x";
        else
        {
            index = 1;
            for(Map.Entry<Player, Integer> entry: list)
            {
                if(entry.getKey().equals(p))
                {
                    place = String.valueOf(index);
                    break;
                }
                index++;
            }
        }
        
        newS = newS
                .replace("{Place}", place)
                .replace("{Name}", p.getName())
                .replace("{Points}", String.valueOf(pointMap.get(p)));
        newS = newS.replace("{TimeLeft}", String.valueOf(timeLeft));
        return newS;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equalsIgnoreCase("Points"))
        {
            return pointMap.get(p) == null ? "0" : String.valueOf(pointMap.get(p));
        }
        else if(stat.equalsIgnoreCase("TotalPoints"))
        {
            int points =  pointMap.get(p) == null ? 0 : pointMap.get(p);
            return getBestAndAdd(p, logic.getName(), "TotalPoints", points);
        }
        else if(stat.equalsIgnoreCase("Time")){
            return String.valueOf(60 - timeLeft);
        }
        throw new IllegalArgumentException("Stat not found");
    }
    
    @Override
    public List<IStatComponent.SQLProperty> getSQL()
    {
        List<IStatComponent.SQLProperty> list = new List();
        list.add(new IStatComponent.SQLProperty("Points", "int(11)", "0", true));
        list.add(new IStatComponent.SQLProperty("TotalPoints", "int(11)", "0", true));
        list.add(new IStatComponent.SQLProperty("Time", "int(11)", "0", true));
        return list;
    }
}
