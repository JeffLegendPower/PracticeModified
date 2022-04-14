package me.trixxtraxx.Practice.ComponentEditor;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Components.*;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sun.security.krb5.internal.crypto.Des;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//this is a class that takes in a player and a List of Components and lets a Player edit them in a Minecraft Inventory GUI.
public class ComponentEditor
{
    private static List<ComponentEditor> editors = new ArrayList<>();
    private Player player;
    private Object editing;
    private List<Component> components;
    private BetterInv gui;
    private Component currentComponent;
    private int page = 0;
    private Save save;
    
    private static ItemStack Description;
    private static ItemStack BackArrow;
    private static ItemStack NextArrow;
    private static ItemStack RemoveComponent;
    private static ItemStack AddComponent;
    private static ItemStack BackToMenu;
    private static HashMap<Class<? extends Component>, ItemStack> componentItems = new HashMap<>();
    
    public static void init(FileConfiguration conf)
    {
        Description = conf.getItemStack("ComponentEditor.DescriptionItem");
        BackArrow = conf.getItemStack("ComponentEditor.BackArrowItem");
        NextArrow = conf.getItemStack("ComponentEditor.NextArrowItem");
        RemoveComponent = conf.getItemStack("ComponentEditor.RemoveComponentItem");
        AddComponent = conf.getItemStack("ComponentEditor.AddComponentItem");
        BackToMenu = conf.getItemStack("ComponentEditor.BackToMenuItem");
        
        componentItems.put(BedLayerComponent.class, new BetterItem(Material.BED));
        componentItems.put(BreakRegion.class, new BetterItem(Material.DIAMOND_PICKAXE));
        componentItems.put(ClearOnDropComponent.class, new BetterItem(Material.BARRIER));
        componentItems.put(NoMapBreakComponent.class, new BetterItem(Material.WOOD_PICKAXE).NsetDurability((short) 1));
        componentItems.put(PlaceRegion.class, new BetterItem(Material.WOOL));
    }
    
    public interface Save{
        void save(List<Component> components);
    }
    
    //this is the constructor for the ComponentEditor.
    public ComponentEditor(Player player, List<Component> components, Save save, Object editing)
    {
        this.player = player;
        this.components = components;
        this.currentComponent = null;
        this.save = save;
        this.editing = editing;
        openComponentGui();
    }
    
    //Create a inventory with the following items:
    //Slot 4 = Book that has a title of "What is This" and a lore of "Components are a important part of a map!"
    //Slots 10-44 excluding 18  19 27  28  36 37 the maps components, make components a Compass
    //Slot 45,36 arrows for pagnation
    //Slot 50 Green wool for adding a component
    //Slot 48 Red wool for removing a component
    //fill up every empty slot thats not for map components with gray glass
    public void openComponentGui()
    {
        gui = new BetterInv(player, 54, "Components");
        renderMenu();
        gui.open();
        gui.onClose(e -> {
            editors.remove(this);
            save.save(components);
        });
    }
    
    private void renderMenu()
    {
        page = 0;
        gui.clear();
        final BetterInv inv = gui;
        inv.setItem(4, Description);
        inv.lock(4);
        
        inv.setItem(50, AddComponent);
        inv.OnClickSlot(50, e -> {
            e.setCancelled(true);
            page = 0;
            gui.clear();
            renderAddComponent();
        });
        
        inv.setItem(48, RemoveComponent);
        inv.onItemPickup(e -> {
            if(e.getSlot() == 48){
                e.setCancelled(true);
            }
        });
        
        inv.onItemPlace(e -> {
            if(e.getSlot() == 48)
            {
                ((InventoryClickEvent)e.getEvent()).setCursor(null);
                e.setCancelled(true);
                if(currentComponent != null) components.remove(currentComponent);
                currentComponent = null;
                inv.clear();
                renderMenu();
            }
        });
        
        inv.setItem(36, BackArrow);
        inv.OnClickSlot(36, e -> {
            e.setCancelled(true);
            if(page > 0) page--;
            renderMenu();
        });
        
        inv.setItem(44, NextArrow);
        inv.OnClickSlot(44, e -> {
            e.setCancelled(true);
            if(page < (components.size() / 28)) page++;
            renderMenu();
        });
        
        int index = 0;
        index += (page * 28);
        for(int i = 10; i < 44; i++)
        {
            if(i != 17 && i != 18 && i != 26 && i != 27 && i != 35 && i != 36)
            {
                //break if components is not long enough
                if(components.size() <= index)
                {
                    //register place event as adding component at the end
                    int finalI = i;
                    inv.onItemPlace(e -> {
                        if(e.getSlot() == finalI)
                        {
                            if(currentComponent != null)
                            {
                                components.remove(currentComponent);
                                components.add(currentComponent);
                                inv.clear();
                                renderMenu();
                            }
                        }
                    });
                    continue;
                }
                //set the Item corresponding to the component at the index
                ItemStack stack = componentItems.get(components.get(index).getClass());
                BetterItem item = new BetterItem(stack);
    
                item.addCustomData("ComponentIndex", index + "");
    
                item.setDisplayName(ChatColor.BLUE + components.get(index).getClass().getSimpleName());
                //get all the parameters of the component
                StringBuilder sb = new StringBuilder();
                for(Field f: components.get(index).getClass().getDeclaredFields())
                {
                    if(f.isAnnotationPresent(Config.class))
                    {
                        try
                        {
                            f.setAccessible(true);
                            //key = Blue, value = aqua
                            sb.append(ChatColor.BLUE).append(f.getName()).append(": ").append(ChatColor.AQUA).append(f.get(
                                    components.get(index)).toString()).append("\n");
                        }
                        catch(IllegalArgumentException|IllegalAccessException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
    
                //append by "\nClick to Edit" + little unicode arrows left and right that is in aqua
                sb.append("\n\n").append(ChatColor.AQUA).append("\u2190").append(" Click to Edit ").append("\u2192");
    
                item.setLore(sb.toString());
                inv.setItem(i, item);
    
                Practice.log(4, "Setting item at " + i + " to " + componentItems.get(components.get(index).getClass()));
    
                int finalIndex = index;
                inv.OnClickSlot(i, e -> {
                    Component c = components.get(finalIndex);
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if(e.getAction() == InventoryAction.PICKUP_ALL)
                            {
                                components.remove(c);
                                currentComponent = c;
                                inv.clear();
                                renderMenu();
                            }
                            else if(e.getAction() == InventoryAction.SWAP_WITH_CURSOR)
                            {
                                if(currentComponent != null)
                                {
                                    components.add(finalIndex, currentComponent);
                                }
                                currentComponent = null;
                                ((InventoryClickEvent) e.getEvent()).setCursor(null);
                                inv.clear();
                                renderMenu();
                            }
                            else if(e.getAction() == InventoryAction.PLACE_ALL)
                            {
                                if(currentComponent != null)
                                {
                                    components.add(finalIndex, currentComponent);
                                    inv.clear();
                                    renderMenu();
                                }
                            }
                        }
                    }.runTaskLater(Practice.Instance,1);
                    //if non of the above actions are performed, then the player is editing the component
                    if(e.getAction() != InventoryAction.PICKUP_ALL && e.getAction() != InventoryAction.PLACE_ALL && e.getAction() != InventoryAction.SWAP_WITH_CURSOR){
                        e.setCancelled(true);
                        inv.clear();
                        renderComponent(c);
                    }
                });
                index++;
            }
        }
        //set gray glass panes for every empty slot that is not a component slot and lock the slots
        int[] slots = new int[]{
                0,1,2,3,5,6,7,8,9,17,18,26,27,35,45,46,47,49,51,52,53
        };
        for(int i : slots)
        {
            inv.setItem(i, new BetterItem(Material.STAINED_GLASS_PANE).NsetDurability((short) 7).setDisplayName(" "));
            inv.lock(i);
        }
    }
    
    private void renderAddComponent()
    {
        page = 0;
        gui.clear();
        final BetterInv inv = gui;
        //set back and forward arrow, make a border with gray glass and put the component classes Listed
        inv.setItem(36, BackArrow);
        inv.OnClickSlot(36, e -> {
            //remove page
            if(page > 0) page--;
            inv.clear();
            renderAddComponent();
        });
        
        inv.setItem(44, NextArrow);
        inv.OnClickSlot(44, e -> {
            //add page
            if(page < (componentItems.size() / 28)) page++;
            inv.clear();
            renderAddComponent();
        });
        
        inv.setItem(49, BackToMenu);
        inv.OnClickSlot(49, e -> {
            e.setCancelled(true);
            page = 0;
            inv.clear();
            renderMenu();
        });
        
        int index = 0;
        index += page * 28;
    
        HashMap<Class<? extends Component>, ItemStack> items = new HashMap<>();
        
        //get all the components from componentItems that have a constructor with only a editing.getClass() parameter
        for(Class<? extends Component> c : componentItems.keySet())
        {
            //check if constructor with only editing.getClass() is available
            try
            {
                c.getConstructor(editing.getClass());
                items.put(c, componentItems.get(c));
            }
            catch(NoSuchMethodException ex)
            {
                //do nothing
            }
        }
        
        for(int i = 10; i < 44; i++)
        {
            if(i != 17 && i != 18 && i != 26 && i != 27 && i != 35 && i != 36)
            {
                //break if components is not long enough
                if(items.size() <= index){break;}
                //set the Item corresponding to the component at the index
                inv.setItem(i, items.get(items.keySet().toArray()[index]));
                
                int finalIndex = index;
                
                inv.OnClickSlot(i, e ->
                {
                    e.setCancelled(true);
                    inv.clear();
                    Class<? extends Component> c = (Class<? extends Component>) items.keySet().toArray()[finalIndex];
                    
                    try
                    {
                        //get new instance with the editing class as a parameter
                        Component comp = c.getConstructor(editing.getClass()).newInstance(editing);
                        Practice.log(4, "Adding component " + comp.getClass().getSimpleName());
                        components.add(comp);
                        renderComponent(comp);
                        return;
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        renderAddComponent();
                    }
                });
                
                index++;
            }
        }
        //set gray glass panes for every empty slot that is not a component slot and lock the slots
        int[] slots = new int[]{
                0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,45,46,47,48,50,51,52,53
        };
        for(int i : slots)
        {
            inv.setItem(i, new BetterItem(Material.STAINED_GLASS_PANE).NsetDurability((short) 7));
            inv.lock(i);
        }
    }
    
    private class ComponentSetting<E>
    {
        private Class<?> c;
        private String name;
        private E value;
        private Config config;
        
        public ComponentSetting(Class<E> c, String name, Object value, Config config)
        {
            this.c = c;
            this.name = name;
            this.value = (E) value;
            this.config = config;
        }
        
        public Class<?> Class()
        {
            return c;
        }
        
        public String getName()
        {
            return name;
        }
        
        public E getValue()
        {
            return value;
        }
        
        public Config getConfig()
        {
            return config;
        }
    }
    
    private void renderComponent(Component c)
    {
        page = 0;
        gui.clear();
        final BetterInv inv = gui;
        List<ComponentSetting> settings = new ArrayList<>();
        //get all properties with @Config annotation and put them into settings List
        for(Field f : c.getClass().getDeclaredFields())
        {
            f.setAccessible(true);
            Practice.log(4, "Field: " + f.getName() + "," + f.isAnnotationPresent(Config.class));
            //check if the field has the Config annotation
            if(f.isAnnotationPresent(Config.class))
            {
                try
                {
                    settings.add(new ComponentSetting(f.getType(), f.getName(), f.get(c), f.getAnnotation(Config.class)));
                }
                catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
        //set back and forwars at slots 48 and 50
        inv.setItem(48, BackArrow);
        inv.OnClickSlot(48, e -> {
            //remove page
            if(page > 0) page--;
            e.setCancelled(true);
            inv.clear();
            renderComponent(c);
        });
        
        inv.setItem(50, NextArrow);
        inv.OnClickSlot(50, e -> {
            //add page
            if(page < (settings.size() / 18)) page++;
            e.setCancelled(true);
            inv.clear();
            renderComponent(c);
        });
        
        inv.setItem(49, BackToMenu);
        inv.OnClickSlot(49, e -> {
            e.setCancelled(true);
            page = 0;
            inv.clear();
            renderMenu();
        });
        
        int index = 0;
        index += page * 18;
        for(int i = 0; i < 36; i++)
        {
            //continue in the 2nd and 3rd row of 4
            if(i < 27 && i >= 9) continue;
            //return if settings is not long enough
            if(settings.size() >= index){break;}
            ComponentSetting setting = settings.get(index);
            //ConfigLocation, Region, String, Integer, bool, Material, ItemStack
            BetterItem item = null;
            inv.lock(i);
            
            //play cool sound
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
            
            if(setting.c == String.class)
            {
                item = new BetterItem(Material.SIGN);
                inv.setItem(i + 9, new BetterItem(Material.BOOK).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + (String) setting.value +ChatColor.AQUA + "\n\n§7Click to change"));
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                   //close inventory and wait for text
                   inv.close();
                });
            }
            else if(setting.c == Boolean.class)
            {
                item = new BetterItem(Material.STONE_BUTTON);
                boolean value = (boolean) setting.value;
                short data = (short) (value ? 10 : 8);
                inv.setItem(i + 9, new BetterItem(Material.INK_SACK, 1, data).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + String.valueOf(value) +ChatColor.AQUA + "\n\nClick to toggle"));
                final int newIndex = i + 9;
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                    //toggle value
                    boolean newvalue = !value;
                    short newdata = (short) (newvalue ? 10 : 8);
                    inv.setItem(newIndex, new BetterItem(Material.INK_SACK, 1, data).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + String.valueOf(value) +ChatColor.AQUA + "\n\nClick to toggle"));
                });
            }
            else if(setting.c == Integer.class)
            {
                item = new BetterItem(Material.PAPER);
                inv.setItem(i + 9, new BetterItem(Material.PAPER).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + String.valueOf(setting.value) + "\n\n" + ChatColor.AQUA + "Right click to increase by 1\nLeft click to decrease by 1"));
                int newIndex = i + 9;
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                    //increment if right else decrement
                    if(e.getAction() == InventoryAction.PICKUP_HALF)
                    {
                        setting.value = (int) setting.value + 1;
                    }
                    else
                    {
                        setting.value = (int) setting.value - 1;
                    }
                    inv.setItem(newIndex, new BetterItem(Material.PAPER).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + String.valueOf(setting.value) + "\n\n" + ChatColor.AQUA + "Right click to increase by 1\nLeft click to decrease by 1"));
                });
            }
            else if(setting.c == Material.class)
            {
                item = new BetterItem(Material.WORKBENCH);
                Material value = (Material) setting.value;
                inv.setItem(i + 9,
                            new BetterItem(value).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(
                                    ChatColor.DARK_BLUE + ((Material) setting.value).name() + "\n\n" + ChatColor.AQUA + "Drag an Item in from your inventory to change it")
                );
                
                final int newIndex = i + 9;
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                });
                
                inv.onItemPlace(e -> {
                    if(e.getSlot() == newIndex)
                    {
                        //replace material
                        setting.value = e.getItem().getType();
                        inv.setItem(newIndex,
                                    new BetterItem(value).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(
                                            ChatColor.DARK_BLUE + ((Material) setting.value).name() + "\n\n" + ChatColor.AQUA + "Drag an Item in from your inventory to change it")
                        );
                    }
                });
            }
            else if(setting.c == ItemStack.class)
            {
                item = new BetterItem(Material.STONE);
                ItemStack value = (ItemStack) setting.value;
                inv.setItem(
                        i + 9,
                        new BetterItem(value).setLore(ChatColor.AQUA + "Drag an Item in from your inventory to change it!")
                );
                
                final int newIndex = i + 9;
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                });
                
                inv.onItemPlace(e -> {
                    if(e.getSlot() == newIndex)
                    {
                        //replace Item
                        setting.value = e.getItem();
                        inv.setItem(
                                newIndex,
                                new BetterItem(value).setLore(ChatColor.AQUA + "Drag an Item in from your inventory to change it!")
                        );
                    }
                });
            }
            else if(setting.c == ConfigLocation.class)
            {
                item = new BetterItem(Material.EMPTY_MAP);
                ConfigLocation value = (ConfigLocation) setting.value;
                //add an Item with the x, y, z of the location in the lore and a text "Click to set to current location"
                inv.setItem(i + 9, new BetterItem(Material.EMPTY_MAP).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + "X: " + value.getX() + "\nY: " + value.getY() + "\nZ: " + value.getZ() + "\n\n" + ChatColor.AQUA + "Click to set to current location"));
    
                final int newIndex = i + 9;
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                    //set to current location
                    setting.value = new ConfigLocation(e.getPlayer().getLocation());
                    inv.setItem(newIndex, new BetterItem(Material.EMPTY_MAP).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + "X: " + ((ConfigLocation) setting.value).getX() + "\nY: " + ((ConfigLocation) setting.value).getY() + "\nZ: " + ((ConfigLocation) setting.value).getZ() + "\n\n" + ChatColor.AQUA + "Click to set to current location"));
                });
            }
            else if(setting.c == Region.class)
            {
                item = new BetterItem(Material.MAP);
                Region value = (Region) setting.value;
                //set and Item with the x, y, z of both region locations and left click to set 1st and right click to set 2nd
                Location loc1 = value.getLocation1(player.getWorld());
                Location loc2 = value.getLocation2(player.getWorld());
                inv.setItem(i + 9, new BetterItem(Material.MAP).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + "X1: " + loc1.getBlockX() + "\nY1: " + loc1.getBlockY() + "\nZ1: " + loc1.getBlockZ() + "\n\nX2: " + loc2.getBlockX() + "\nY2: " + loc2.getBlockY() + "\nZ2: " + loc2.getBlockZ() + "\n\n" + ChatColor.AQUA + "Left click to set 1st location\nRight click to set 2nd location"));
                
                final int newIndex = i + 9;
                inv.OnClickSlot(i + 9, e -> {
                    e.setCancelled(true);
                    if(e.getAction() == InventoryAction.PICKUP_HALF)
                    {
                        //set 1st location
                        setting.value = new Region(player.getLocation(),((Region) setting.value).getLocation2(player.getWorld()));
                    }
                    else
                    {
                        //set 2nd location
                        setting.value = new Region(((Region) setting.value).getLocation1(player.getWorld()),player.getLocation());
                    }
                    //update Item
                    Location newLoc1 = ((Region) setting.value).getLocation1(player.getWorld());
                    Location newLoc2 = ((Region) setting.value).getLocation2(player.getWorld());
                    inv.setItem(newIndex, new BetterItem(Material.MAP).setDisplayName(ChatColor.DARK_BLUE + "Set " + setting.getName()).setLore(ChatColor.DARK_BLUE + "X1: " + newLoc1.getBlockX() + "\nY1: " + newLoc1.getBlockY() + "\nZ1: " + newLoc1.getBlockZ() + "\n\nX2: " + newLoc2.getBlockX() + "\nY2: " + newLoc2.getBlockY() + "\nZ2: " + newLoc2.getBlockZ() + "\n\n" + ChatColor.AQUA + "Left click to set 1st location\nRight click to set 2nd location"));
                });
            }
            if(item != null) item.setDisplayName(ChatColor.DARK_BLUE + setting.getName()).setLore(ChatColor.AQUA + setting.getConfig().description());
            inv.setItem(i, item);
        }
    }
    
    public static ComponentEditor getEditor(Player p)
    {
        for(ComponentEditor editor : editors)
        {
            if(editor.player.equals(p))
            {
                return editor;
            }
        }
        return null;
    }
}