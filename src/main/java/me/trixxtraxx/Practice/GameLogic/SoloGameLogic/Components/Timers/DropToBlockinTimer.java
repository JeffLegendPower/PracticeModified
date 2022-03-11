package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropToBlockinTimer extends TimerComponent
{

    public DropToBlockinTimer(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof BlockPlaceEvent) onBlockPlace((BlockPlaceEvent) event);
    }

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof DropEvent) onDrop((DropEvent) event);
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    public void onBlockPlace(BlockPlaceEvent e)
    {
        List<Block> checked = new ArrayList<>();

        Block source = e.getPlayer().getEyeLocation().getBlock();

        List<Block> current = new ArrayList<>();
        current.add(source);

        int depth = 1;
        while(depth < 7)
        {
            if(current.isEmpty()) stop();

            List<Block> copy = new ArrayList<>(current);
            current.clear();
            for (Block b:copy)
            {
                for (Block b2:new Block[]{b.getRelative(BlockFace.DOWN), b.getRelative(BlockFace.UP), b.getRelative(BlockFace.NORTH), b.getRelative(BlockFace.EAST), b.getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.WEST)})
                {
                    if(checked.contains(b2)) continue;
                    checked.add(b2);

                    if(b2.getType() == Material.AIR) {current.add(b2);}
                }
            }
            depth++;
        }
    }

    public void onDrop(DropEvent e){
        reset();
        start();
    }

    public void onReset(ResetEvent e){
        stop();
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{BlockinTimer}", getTime());
    }
}
