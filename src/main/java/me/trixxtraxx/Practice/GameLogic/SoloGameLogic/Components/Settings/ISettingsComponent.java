package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import org.bukkit.inventory.ItemStack;

public interface ISettingsComponent
{
    public int getSlot();
    public BetterItem getItem();
    public List<subSetting> getSubSettings();
    public void onSetSubSetting();
    
    public static class subSetting
    {
        public BetterItem item;
        public int slot;
        public subSettingExecute execute;
        
        public subSetting(BetterItem item, int slot, subSettingExecute execute)
        {
            this.item = item;
            this.slot = slot;
            this.execute = execute;
        }
    }
    
    public static interface subSettingExecute
    {
        public void execute(subSetting setting);
    }
}
