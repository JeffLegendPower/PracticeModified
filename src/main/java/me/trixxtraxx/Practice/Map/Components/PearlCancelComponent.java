package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PearlCancelComponent extends MapComponent {

    @Config
    public int min_y;
    @Config
    public int max_y;

    public PearlCancelComponent(Map map) {
        super(map);
    }

    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            if (event.getTo().getY() < min_y || event.getTo().getY() > max_y) {
                player.sendMessage(ChatColor.RED + "Teleporting above " + max_y + " or below " + min_y + " is disabled in this arena");
                event.setCancelled(true);
            }
        }
    }
}
