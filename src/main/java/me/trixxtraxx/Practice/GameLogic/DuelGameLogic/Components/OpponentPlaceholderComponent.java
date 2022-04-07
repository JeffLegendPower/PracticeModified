package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpponentPlaceholderComponent extends GameComponent
{

    public OpponentPlaceholderComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        Player op = null;
        for (Player p2:logic.getPlayers())
        {
            if(p2 != p) op = p2;
        }
        return s
                .replace("{OpponentName}", op.getDisplayName())
                .replace("{OpponentPing}", ((CraftPlayer)op).getHandle().ping + "");

    }
}
