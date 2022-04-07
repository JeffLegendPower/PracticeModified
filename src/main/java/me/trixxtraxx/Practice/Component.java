package me.trixxtraxx.Practice;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.lang.reflect.Field;

public abstract class Component
{
    public String getData()
    {
        //get all properties with the @Config annotation
        //and serialize them to a string
        StringBuilder sb = new StringBuilder();
        for(Field f : this.getClass().getDeclaredFields())
        {
            if(f.isAnnotationPresent(Config.class))
            {
                try
                {
                    f.setAccessible(true);
                    sb.append(f.getName()).append("=").append(f.get(this)).append("\n");
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    
    public void applyData(String data)
    {
        //deserialize the data that got serialized by getData()
        //and apply it to the properties
        String[] lines = data.split("\n");
        for(String line : lines)
        {
            String[] parts = line.split("=");
            if(parts.length == 2)
            {
                try
                {
                    Field f = this.getClass().getDeclaredField(parts[0]);
                    f.setAccessible(true);
                    f.set(this, parts[1]);
                }
                catch (NoSuchFieldException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public String applyPlaceholder(Player p, String s){return s;}
}
