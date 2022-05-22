package me.trixxtraxx.Practice.Bungee.Queue;

public class QueueUpdatePacket
{
    private String player;
    private boolean inQueue;
    
    public QueueUpdatePacket(final String player, final boolean inQueue) {
        this.player = player;
        this.inQueue = inQueue;
    }
    
    public String getPlayer() {
        return this.player;
    }
    
    public boolean isInQueue() {
        return this.inQueue;
    }
}
