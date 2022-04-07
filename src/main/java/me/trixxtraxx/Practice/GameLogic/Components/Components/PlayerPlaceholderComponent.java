package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerPlaceholderComponent extends GameComponent
{

    public PlayerPlaceholderComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s
                .replace("{PlayerName}", p.getDisplayName())
                .replace("{PlayerPing}", ((CraftPlayer)p).getHandle().ping + "");
    }
}
