package me.trixxtraxx.Practice.GameLogic.Components.Components.InventoryView;

import com.google.gson.Gson;
import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.RestCommunicator.PluginAPI.MessageProvider;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.WinMessageComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class InventoryViewComponent extends GameComponent
{
    public InventoryViewComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = -1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onGameEnd(WinEvent event)
    {
        logic.broadcast("§9Inventories:");
        for(Player player : logic.getPlayers())
        {
            PlayerInventory inv = player.getInventory();
            List<ItemStack> Items = new List<>();
            List<Integer> Slots = new List<>();
            for(int i = 0; i < 36; i++)
            {
                ItemStack item = inv.getItem(i);
                if(item != null)
                {
                    Items.add(item);
                    Slots.add(i);
                }
            }
            
            if(inv.getHelmet() != null)
            {
                Items.add(inv.getHelmet());
                Slots.add(36);
            }
            if(inv.getChestplate() != null)
            {
                Items.add(inv.getChestplate());
                Slots.add(37);
            }
            if(inv.getLeggings() != null)
            {
                Items.add(inv.getLeggings());
                Slots.add(38);
            }
            if(inv.getBoots() != null)
            {
                Items.add(inv.getBoots());
                Slots.add(39);
            }
            Items.add(new BetterItem(Material.REDSTONE, (int) player.getHealth()).setDisplayName("§9Health: §b" + player.getHealth()));
            Slots.add(44);
            int potions = new List<>(inv.getContents()).findAll(x -> x.getType() == Material.POTION).size();
            Items.add(new BetterItem(Material.POTION, potions).setDisplayName("§9Potions: §b" + potions));
            Slots.add(45);
            
            InventoryView view = new InventoryView();
            view.ItemStacks = BetterItem.serialize((ItemStack[]) Items.toArray());
            view.Slots = Slots;
            view.uuid = UUID.randomUUID().toString();
    
            MessageProvider.SendMessage("Practice_InventoryView_Add", new Gson().toJson(view));
    
            Practice.log(4, "Inventory of " + player.getName() + ": " + view.uuid);
            
            for(Player send : logic.getPlayers())
            {
                TextComponent component = new TextComponent(TextComponent.fromLegacyText("§b" + player.getName() + "§f Click to View his Inventory"));
                // Add a click event to the component.
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/InventoryView " + view.uuid ));
    
                // Send it!
                send.spigot().sendMessage(component);
            }
        }
    }
}
