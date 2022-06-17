package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class KitSettings extends GameComponent implements ISettingsComponent
{
    @Config
    public int slot;
    @Config
    public ItemStack item;
    @Config
    public List<KitSetting> kits;
    
    public KitSettings(GameLogic logic, int slot, ItemStack item, List<KitSetting> kits)
    {
        super(logic);
        this.slot = slot;
        this.item = item;
        this.kits = kits;
    }
    
    public KitSettings(GameLogic logic){super(logic);}
    
    public class KitSetting
    {
        public int slot;
        public ItemStack item;
        public String kit;
        
        public KitSetting(int slot, ItemStack item, String kit)
        {
            this.slot = slot;
            this.item = item;
            this.kit = kit;
        }
    }
    
    @Override
    public int getSlot()
    {
        return slot;
    }
    
    @Override
    public BetterItem getItem()
    {
        return new BetterItem(item);
    }
    
    @Override
    public List<subSetting> getSubSettings()
    {
        List<subSetting> settings = new List<subSetting>();
        for(KitSetting kit : kits)
        {
            settings.add(new subSetting(new BetterItem(kit.item), kit.slot, (setting) -> {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Kit k = SQLUtil.Instance.getKit(kit.kit);
                        logic.getGame().setKit(k);
                    }
                }.runTaskAsynchronously(Practice.Instance);
            }));
        }
        return settings;
    }
    
    @Override
    public void onSetSubSetting(){}
    
    @Override
    public String getData()
    {
        String data = "";
        BetterItem item = new BetterItem(this.item);
        data += slot + "<>" + item.toJson() + "<>";
        for(KitSetting kit : kits)
        {
            BetterItem kitItem = new BetterItem(kit.item);
            data += kit.slot + "<>" + kitItem.toJson() + "<>" + kit.kit + "<>";
        }
        return data;
    }
    
    @Override
    public void applyData(String data)
    {
        String[] split = data.split("<>");
        slot = Integer.parseInt(split[0]);
        item = BetterItem.fromJson(split[1]);
        for(int i = 2; i < split.length; i += 3)
        {
            int slot = Integer.parseInt(split[i]);
            ItemStack item = BetterItem.fromJson(split[i + 1]);
            String kit = split[i + 2];
            kits.add(new KitSetting(slot, item, kit));
        }
    }
}
