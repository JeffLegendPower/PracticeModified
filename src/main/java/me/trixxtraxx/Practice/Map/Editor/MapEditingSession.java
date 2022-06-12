package me.trixxtraxx.Practice.Map.Editor;

import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.ComponentEditor.ComponentEditor;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;
import org.bukkit.scheduler.BukkitRunnable;

public class MapEditingSession
{
    private static List<MapEditingSession> sessions = new List<>();
    private Player player;
    private Map map;
    private World world;

    public MapEditingSession(Player player, Map m){
        this.player = player;
        this.map = m;
        world = m.load();
        player.teleport(world.getSpawnLocation());
    }

    public static void addSession(MapEditingSession session) {sessions.add(session);}
    public static void removeSession(MapEditingSession session){
        sessions.remove(session);
        if(session != null) session.exit(true);
    }
    public static MapEditingSession getSession(Player player){
        for (MapEditingSession session : sessions)
        {
            if (session.getPlayer() == player)
            {
                return session;
            }
        }
        return null;
    }
    public static List<MapEditingSession> getSessions() {return sessions;}

    public Player getPlayer() {return player;}
    public Map getMap() {return map;}
    
    public World getWorld() {return map == null ? null : map.getWorld();}
    public void exit(boolean save){
        map.unload(true);
        if (save)
        {
            new BukkitRunnable(){
                @Override
                public void run(){
                    SQLUtil.Instance.deleteMap(map);
                    SQLUtil.Instance.addMap(map);
                }
            }.runTaskAsynchronously(Practice.Instance);
        }
        //tp to lobby
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }
    public void openComponentGui(){
        List<Component> components = new List<>();
        for(MapComponent mc : map.getComponents())
        {
            components.add(mc);
        }
        new ComponentEditor(player, components, comps ->
        {
            List<MapComponent> newcomps = new List<>();
            for(Component c : comps)
            {
                newcomps.add((MapComponent) c);
            }
            map.setComponents(newcomps);
        } , map);
    }
}