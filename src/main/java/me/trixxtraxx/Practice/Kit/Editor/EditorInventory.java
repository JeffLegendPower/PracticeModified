package me.trixxtraxx.Practice.Kit.Editor;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.Linq.List;
import org.bukkit.entity.Player;

public abstract class EditorInventory
{
    protected static List<EditorInventory> editors = new List<>();
    protected Player p;
    protected BetterInv inv;
    
    public EditorInventory(Player p, String name, int size)
    {
        this.p = p;
        inv = new BetterInv(this.p, size, name);
        open();
    }
    
    public void open()
    {
        fill();
        inv.open();
    }
    public void close()
    {
        inv.close();
    }
    
    public abstract void fill();
}
