package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class WinMessageComponent extends GameComponent
{
    @Config
    public String winMessage;
    
    public WinMessageComponent(GameLogic logic, String winMessage)
    {
        super(logic);
        this.winMessage = winMessage;
    }
    
    public WinMessageComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onWin(WinEvent event)
    {
        Practice.log(4, "Sending win message");
        String msg = logic.applyPlaceholders(event.getPlayer(),
                     winMessage
                     .replace("{Winner}", event.getPlayer().getName())
        );
        if(logic instanceof DuelGameLogic){
            DuelGameLogic duel = (DuelGameLogic)logic;
            Player winner = event.getPlayer();
            Player loser = duel.getP1() == winner ? duel.getP2() : duel.getP1();
            msg = msg.replace("{Loser}", loser.getName());
            msg = msg.replace("{LoserHealth}", String.valueOf((int) loser.getHealth()));
            msg = msg.replace("{WinnerHealth}", String.valueOf((int) winner.getHealth()));
        }
        for(Player player : logic.getPlayers())
        {
            for(String line : msg.split("\n"))
            {
                player.sendMessage(line);
            }
        }
    }
}
