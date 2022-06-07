package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BedComponent extends GameComponent
{
    protected boolean t1Bed = true;
    protected boolean t2Bed = true;
    
    public BedComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onBedBreak(BlockBreakEvent event){
        if(event.getBlock().getType() == org.bukkit.Material.BED_BLOCK || event.getBlock().getType() == Material.BED){
            Location loc = event.getBlock().getLocation();
            if(!(logic instanceof DuelGameLogic)) return;
            DuelGameLogic duelLogic = (DuelGameLogic)logic;
            ISpawnComponent spawn = logic.getMap().getSpawn();
            
            Location t1Spawn = spawn.getSpawn(logic, duelLogic.getP1());
            Location t2Spawn = spawn.getSpawn(logic, duelLogic.getP2());
            
            double dist1 = loc.distance(t1Spawn);
            double dist2 = loc.distance(t2Spawn);
            
            if(dist1 < dist2)
            {
                if(duelLogic.getP1() == event.getPlayer()) {
                    event.setCancelled(true);
                    return;
                }
                t1Bed = false;
            }
            else
            {
                if(duelLogic.getP2() == event.getPlayer()) {
                    event.setCancelled(true);
                    return;
                }
                t2Bed = false;
            }
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onDeath(PlayerDeathEvent event)
    {
        if(!(logic instanceof DuelGameLogic)) return;
        DuelGameLogic duelLogic = (DuelGameLogic)logic;
        if(event.getEntity() == duelLogic.getP1())
        {
            Practice.log(4, "Player 1: " + t1Bed);
            if(t1Bed) return;
        }
        else if(event.getEntity() == duelLogic.getP2()){
            Practice.log(4, "Player 2: " + t2Bed);
            if(t2Bed) return;
        }
        else return;
        Practice.log(4, "checking who is winning");
        Player winner = null;
        if(event.getEntity() == duelLogic.getP1()) winner = duelLogic.getP2();
        if(event.getEntity() == duelLogic.getP2()) winner = duelLogic.getP1();
        duelLogic.win(winner, false);
    }
    
    @Override
    public String applyPlaceholder(Player p , String s){
        String newS = s;
        if(logic instanceof DuelGameLogic)
        {
            DuelGameLogic duelLogic = (DuelGameLogic)logic;
            if(p == duelLogic.getP1()) newS = newS.replace("{PlayerBed}", t1Bed ? "✔" : "✖").replace("{OpponentBed}", t2Bed ? "✔" : "✖");
            if(p == duelLogic.getP2()) newS = newS.replace("{PlayerBed}", t2Bed ? "✔" : "✖").replace("{OpponentBed}", t1Bed ? "✔" : "✖");
        }
        return newS;
    }
}
