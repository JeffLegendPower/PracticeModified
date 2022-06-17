package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SettingsComponent extends GameComponent
{
    @Config
    public Material openMat = Material.NETHER_STAR;
    @Config
    private String title = ChatColor.BLUE + "Settings";
    @Config
    private int size = 27;

    public SettingsComponent(GameLogic logic, Material open, String title, int size)
    {
        super(logic);
        this.title = title;
        this.openMat = open;
        this.size = size;
    }
    public SettingsComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(e.getItem() != null && e.getItem().getType() == openMat)
        {
            e.setCancelled(true);
            openInv(p);
        }
    }
    
    public void openInv(Player p){
        BetterInv inv = new BetterInv(p, size, title);
        setMain(inv);
        inv.open();
    }
    
    public void setMain(BetterInv inv)
    {
        List<ISettingsComponent> settings = logic.getComponents(ISettingsComponent.class).cast(x -> (ISettingsComponent) x);
        for(ISettingsComponent setting : settings)
        {
            inv.setItem(setting.getSlot(), setting.getItem());
            inv.onClickSlot(setting.getSlot(), (event) ->
            {
                event.setCancelled(true);
                inv.clear();
                openSubInv(inv, setting);
            });
        }
    }
    
    public void openSubInv(BetterInv inv, ISettingsComponent settings)
    {
        for(ISettingsComponent.subSetting sub : settings.getSubSettings())
        {
            inv.setItem(sub.slot, sub.item);
            inv.onClickSlot(sub.slot, (event1) ->
            {
                event1.setCancelled(true);
                sub.execute.execute(sub);
            });
        }
        settings.onSetSubSetting();
    }
}

