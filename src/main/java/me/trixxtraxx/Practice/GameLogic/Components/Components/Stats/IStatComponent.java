package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.Map;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface IStatComponent
{
    public class SQLProperty
    {
        private String name;
        private String type;
        private boolean isPerGame;
        
        public SQLProperty(String name, String type, boolean isPerGame)
        {
            this.name = name;
            this.type = type;
            this.isPerGame = isPerGame;
        }
        
        public String getName()
        {
            return name;
        }
        
        public String getType()
        {
            return type;
        }
        
        public boolean isPerGame()
        {
            return isPerGame;
        }
    }
    
    public String getStat(Player p, String stat);
    public List<SQLProperty> getSQL();
}