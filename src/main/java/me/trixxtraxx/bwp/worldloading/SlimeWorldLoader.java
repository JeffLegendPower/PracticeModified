package me.trixxtraxx.bwp.worldloading;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.World;

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
            world = world.clone(name + "-" + mapCount);
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
        Bukkit.unloadWorld(world, false);
    }
}
