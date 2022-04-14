package me.trixxtraxx.Practice;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.SQL.ConfigItem;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;

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
                    String name = f.getName();
                    String value = f.get(this).toString();
                    if(f.getType() == ItemStack.class)
                    {
                        ItemStack i = (ItemStack) f.get(this);
                        value = new Gson().toJson(i.serialize());
                    }
                    else if(f.getType() == Material.class)
                    {
                        value = ((Material) f.get(this)).toString();
                    }
                    else if(f.getType() == ConfigLocation.class)
                    {
                        value = ((ConfigLocation)f.get(this)).serialize();
                    }
                    else if(f.getType() == Region.class)
                    {
                        value = ((Region)f.get(this)).serialize();
                    }
                    Practice.log(4,name + " = " + value);
                    sb.append(name).append("=").append(value).append("\n");
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
                    
                    //deserialize to varios things if needed
                    if(f.getType() == Material.class)
                    {
                        f.set(this, Material.valueOf(parts[1]));
                    }
                    else if(f.getType() == ItemStack.class)
                    {
                        //make to Map<String, Object>
                        java.util.Map<String, Object> mapClass = new HashMap<String, Object>();
                        f.set(this, ItemStack.deserialize(new Gson().fromJson(parts[1], mapClass.getClass())));
                    }
                    else if(f.getType() == ConfigLocation.class)
                    {
                        f.set(this, ConfigLocation.deserialize(parts[1]));
                    }
                    else if(f.getType() == Region.class)
                    {
                        f.set(this, Region.deserialize(parts[1]));
                    }
                    else if(f.getType() == boolean.class)
                    {
                        f.set(this, Boolean.parseBoolean(parts[1]));
                    }
                    else if(f.getType() == int.class)
                    {
                        f.set(this, Integer.parseInt(parts[1]));
                    }
                    else if(f.getType() == double.class)
                    {
                        f.set(this, Double.parseDouble(parts[1]));
                    }
                    else if(f.getType() == float.class)
                    {
                        f.set(this, Float.parseFloat(parts[1]));
                    }
                    else if(f.getType() == long.class)
                    {
                        f.set(this, Long.parseLong(parts[1]));
                    }
                    else
                    {
                        f.set(this, parts[1]);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public String applyPlaceholder(Player p, String s){return s;}
}
