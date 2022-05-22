package me.trixxtraxx.Practice.Bungee;

import java.util.HashMap;

public class KitOrderUpdatePacket
{
    private String player;
    private int kitId;
    private HashMap<Integer, Integer> order;
    
    public KitOrderUpdatePacket(final String player, final int kitId, final HashMap<Integer, Integer> order) {
        this.player = player;
        this.kitId = kitId;
        this.order = order;
    }
    
    public String getPlayer() {
        return this.player;
    }
    
    public int getKitId() {
        return this.kitId;
    }
    
    public HashMap<Integer, Integer> getOrder() {
        return this.order;
    }
}
