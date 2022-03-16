package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MapNamePlaceholderComponent extends GameComponent
{
    public MapNamePlaceholderComponent(GameLogic logic)
    {
        super(logic);
    }
    public MapNamePlaceholderComponent(GameLogic logic, String s)
    {
        super(logic);
    }
    @Override
    public String getData() {return "{}";}

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{MapName}", logic.getMap().getName());
    }
}
