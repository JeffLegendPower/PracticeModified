package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectComponent extends GameComponent
{
    @Config
    public PotionEffectType effect;
    @Config
    public int amplifier;
    
    public EffectComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onStart(StartEvent event)
    {
        for(Player p : logic.getPlayers())
        {
            p.addPotionEffect(new PotionEffect(effect, 999999, amplifier));
        }
    }
    
    @Override
    public String getData()
    {
        return "effect=" + effect.toString() + "<>amplifier=" + amplifier;
    }
    
    @Override
    public void applyData(String value)
    {
        String[] split = value.split("<>");
        for(String s : split)
        {
            String[] split2 = s.split("=");
            if(split2[0].equalsIgnoreCase("effect"))
            {
                effect = PotionEffectType.getByName(split2[1]);
            }
            else if(split2[0].equalsIgnoreCase("amplifier"))
            {
                amplifier = Integer.parseInt(split2[1]);
            }
        }
    }
}
