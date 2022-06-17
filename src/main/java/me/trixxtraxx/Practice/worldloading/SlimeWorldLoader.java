package me.trixxtraxx.Practice.worldloading;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SlimeWorldLoader implements WorldLoader
{
    private SlimeLoader loader;
    private SlimePlugin plugin;
    private int mapCount = 0;

    public SlimeWorldLoader(SlimePlugin plug, SlimeLoader load)
    {
        loader = load;
        plugin = plug;
    }

    @Override
    public World loadWorld(String name)
    {
        try
        {
            final SlimePropertyMap properties = new SlimePropertyMap();
            properties.setString(SlimeProperties.DIFFICULTY, "normal");
            properties.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
            properties.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
            properties.setBoolean(SlimeProperties.PVP, true);
            properties.setInt(SlimeProperties.SPAWN_X, 0);
            properties.setInt(SlimeProperties.SPAWN_Y, 100);
            properties.setInt(SlimeProperties.SPAWN_Z, 0);
            properties.setString(SlimeProperties.ENVIRONMENT, "NORMAL");
            properties.setString(SlimeProperties.WORLD_TYPE, "default");

            SlimeWorld world = plugin.loadWorld(loader, name, true, properties);
            mapCount++;
            world = world.clone(name + "-" + mapCount + new Random().nextInt(10000));
            plugin.generateWorld(world);
            return Bukkit.getWorld(world.getName());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void unloadWorld(World world)
    {
        new BukkitRunnable(){
        
            @Override
            public void run()
            {
                Bukkit.unloadWorld(world, false);
                for(Player p : world.getPlayers()){
                    BungeeUtil.getInstance().toLobby(p);
                }
            }
        }.runTaskLater(Practice.Instance, 10);
        new BukkitRunnable(){
    
            @Override
            public void run()
            {
                for(Player p : world.getPlayers()){
                    p.kickPlayer("§cSomething went wrong!");
                }
                Bukkit.unloadWorld(world, false);
            }
        }.runTaskLater(Practice.Instance, 200);
    
        new BukkitRunnable(){
        
            @Override
            public void run()
            {
                for(Player p : world.getPlayers()){
                    p.kickPlayer("§cSomething went wrong!");
                }
                Bukkit.unloadWorld(world, false);
            }
        }.runTaskLater(Practice.Instance, 2000);
    }
}
