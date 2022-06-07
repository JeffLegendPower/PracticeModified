package me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class GenerateLinesPlaceholder extends GameComponent
{
    @Config
    public String replace;
    @Config
    public String line;
    
    public GenerateLinesPlaceholder(GameLogic logic)
    {
        super(logic);
    }
    
    public GenerateLinesPlaceholder(GameLogic logic, String replace, String line)
    {
        super(logic);
        this.replace = replace;
        this.line = line;
    }
    
    @Override
    public String applyPlaceholder(Player p, String s)
    {
        if(s.contains(replace))
        {
            String lines = "";
            for(int i = 0; i < logic.getPlayers().size(); i++)
            {
                lines += line.replace("{line}", (i + 1) + "");
                lines += "\n";
            }
            return s.replace(replace, lines);
        }
        else return s;
    }
}
