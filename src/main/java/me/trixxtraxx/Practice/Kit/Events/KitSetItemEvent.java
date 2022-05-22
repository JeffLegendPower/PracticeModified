package me.trixxtraxx.Practice.Kit.Events;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.SQL.PracticePlayer;

public class KitSetItemEvent extends GameEvent
{
    private BetterItem item;
    private PracticePlayer pp;
    private int slot;
    private int itemIndex;
    
    public KitSetItemEvent(GameLogic logic, final PracticePlayer pp, final BetterItem item, final int slot, final int itemIndex) {
        super(logic);
        this.pp = pp;
        this.item = item;
        this.slot = slot;
        this.itemIndex = itemIndex;
    }
    
    public BetterItem getItem() {
        return this.item;
    }
    
    public PracticePlayer getPracticePlayer() {
        return this.pp;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public int getItemIndex() {
        return this.itemIndex;
    }
    
    public void setItem(final BetterItem item) {
        this.item = item;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }
}