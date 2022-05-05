package me.trixxtraxx.Practice;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.CustomValue;
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
        if(CustomValue.class.isAssignableFrom(this.getClass()))
        {
            return ((CustomValue)this).getValue();
        }
        for(Field f : this.getClass().getDeclaredFields())
        {
            if(f.isAnnotationPresent(Config.class))
            {
                try
                {
                    f.setAccessible(true);
                    String name = f.getName();
                    Object v = f.get(this);
                    String value = "NULL";
                    if(v != null)
                    {
                        value = new Gson().toJson(v);
                        //first, handle ItemStack
                        //use toString() for Material, ConfigLocation, Region, String,boolean, int, long, double, float, short, byte
                        if(f.getType() == ItemStack.class)
                        {
                            ItemStack i = (ItemStack) f.get(this);
                            value = new Gson().toJson(i.serialize());
                        }
                        else if(f.getType() == Material.class ||
                                f.getType() == ConfigLocation.class ||
                                f.getType() == Region.class ||
                                f.getType() == String.class ||
                                f.getType() == boolean.class ||
                                f.getType() == int.class ||
                                f.getType() == long.class ||
                                f.getType() == double.class ||
                                f.getType() == float.class ||
                                f.getType() == short.class ||
                                f.getType() == byte.class)
                        {
                            value = v.toString();
                        }
                    }
                    Practice.log(4,name + " = " + value);
                    sb.append(name).append("=").append(value).append("<>");
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
        Practice.log(4,"Applying data to component " + this.getClass().getSimpleName() + ": " + data);
        if(CustomValue.class.isAssignableFrom(this.getClass()))
        {
            try
            {
                ((CustomValue) this).applyValue(data);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        String[] lines = data.split("<>");
        Practice.log(4,"Lines: " + lines.length);
        for(String line : lines)
        {
            Practice.log(4,"Line: " + line);
            String[] parts = line.split("=");
            if(parts.length == 2)
            {
                try
                {
                    Field f = this.getClass().getDeclaredField(parts[0]);
                    f.setAccessible(true);
                    Practice.log(4,"Setting value of " + parts[0] + " to " + parts[1]);
                    
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
                    else if(f.getType() == short.class)
                    {
                        f.set(this, Short.parseShort(parts[1]));
                    }
                    else if(f.getType() == byte.class)
                    {
                        f.set(this, Byte.parseByte(parts[1]));
                    }
                    else if(f.getType() == String.class)
                    {
                        f.set(this, parts[1].replace("\\n", "\n"));
                    }
                    else
                    {
                        f.set(this, new Gson().fromJson(parts[1],f.getType()));
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
