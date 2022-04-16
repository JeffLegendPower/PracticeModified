package me.trixxtraxx.Practice.Map.Editor;

import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.ComponentEditor.ComponentEditor;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MapEditingSession
{
    private static List<MapEditingSession> sessions = new ArrayList<>();
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
        session.exit(true);
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

    public World getWorld() {return world;}

    public void exit(boolean save){
        map.unload(true);
        if (save)
        {
            SQLUtil.Instance.deleteMap(map);
            SQLUtil.Instance.addMap(map);
        }
        //tp to lobby
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        removeSession(this);
    }
    
    public void openComponentGui(){
        List<Component> components = new ArrayList<>();
        for(MapComponent mc : map.getComponents())
        {
            components.add(mc);
        }
        new ComponentEditor(player, components, comps ->
        {
            List<MapComponent> newcomps = new ArrayList<>();
            for(Component c : comps)
            {
                newcomps.add((MapComponent) c);
            }
            map.setComponents(newcomps);
        } , map);
    }
}