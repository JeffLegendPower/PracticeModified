package me.trixxtraxx.Practice.Kit.Editor.Enchants;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EnchantmentCategory
{
    protected static List<EnchantmentCategory> categories = new List<EnchantmentCategory>();
    protected HashMap<Enchantment, ItemStack> enchants;
    protected List<isItem> items;
    
    public interface isItem
    {
        boolean isItem(ItemStack item);
    }
    
    public EnchantmentCategory(HashMap<Enchantment, ItemStack> enchants, List<isItem> items) {
        this.enchants = enchants;
        this.items = items;
        EnchantmentCategory.categories.add(this);
    }
    
    public static List<EnchantmentCategory> getCategories(ItemStack item)
    {
        return categories.findAll(x -> x.items.any(y -> y.isItem(item)));
    }
    
    public HashMap<Enchantment, ItemStack> getEnchants() {
        return enchants;
    }
    
    public static void init(){
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.LUCK, new BetterItem(Material.RAW_FISH).setDisplayName("§9Luck of the Sea"));
                put(Enchantment.LURE, new BetterItem(Material.FISHING_ROD).setDisplayName("§9Lure"));
            }},
            new List<isItem>(x -> x.getType() == Material.FISHING_ROD)
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.ARROW_DAMAGE, new BetterItem(Material.ARROW).setDisplayName("§9Power"));
                put(Enchantment.ARROW_FIRE, new BetterItem(Material.FIREBALL).setDisplayName("§9Flame"));
                put(Enchantment.ARROW_INFINITE, new BetterItem(Material.BOW).setDisplayName("§9Infinity"));
                put(Enchantment.ARROW_KNOCKBACK, new BetterItem(Material.ARROW).setDisplayName("§9Punch"));
            }},
            new List<isItem>(x -> x.getType() == Material.BOW)
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.DAMAGE_ALL, new BetterItem(Material.DIAMOND_SWORD).setDisplayName("§9Sharpness"));
                put(Enchantment.DAMAGE_ARTHROPODS, new BetterItem(Material.SPIDER_EYE).setDisplayName("§9Bane of Arthropods"));
                put(Enchantment.DAMAGE_UNDEAD, new BetterItem(Material.ROTTEN_FLESH).setDisplayName("§9Smite"));
                put(Enchantment.KNOCKBACK, new BetterItem(Material.STICK).setDisplayName("§9Knockback"));
                put(Enchantment.FIRE_ASPECT, new BetterItem(Material.FIREBALL).setDisplayName("§9Fire Aspect"));
                put(Enchantment.LOOT_BONUS_MOBS, new BetterItem(Material.GOLD_SWORD).setDisplayName("§9Looting"));
            }},
            new List<isItem>(x ->
                    x.getType() == Material.DIAMOND_SWORD || x.getType() == Material.IRON_SWORD || x.getType() == Material.GOLD_SWORD || x.getType() == Material.STONE_SWORD || x.getType() == Material.WOOD_SWORD
            )
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.DIG_SPEED, new BetterItem(Material.DIAMOND_PICKAXE).setDisplayName("§9Efficiency"));
                put(Enchantment.LOOT_BONUS_BLOCKS, new BetterItem(Material.GOLD_PICKAXE).setDisplayName("§9Fortune"));
                put(Enchantment.SILK_TOUCH, new BetterItem(Material.COAL_ORE).setDisplayName("§9Silk Touch"));
            }},
            new List<isItem>(x ->
                 x.getType() == Material.DIAMOND_PICKAXE || x.getType() == Material.IRON_PICKAXE || x.getType() == Material.GOLD_PICKAXE || x.getType() == Material.STONE_PICKAXE || x.getType() == Material.WOOD_PICKAXE ||
                 x.getType() == Material.DIAMOND_AXE || x.getType() == Material.IRON_AXE || x.getType() == Material.GOLD_AXE || x.getType() == Material.STONE_AXE || x.getType() == Material.WOOD_AXE ||
                 x.getType() == Material.DIAMOND_SPADE || x.getType() == Material.IRON_SPADE || x.getType() == Material.GOLD_SPADE || x.getType() == Material.STONE_SPADE || x.getType() == Material.WOOD_SPADE ||
                 x.getType() == Material.DIAMOND_HOE || x.getType() == Material.IRON_HOE || x.getType() == Material.GOLD_HOE || x.getType() == Material.STONE_HOE || x.getType() == Material.WOOD_HOE
            )
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.DURABILITY, new BetterItem(Material.IRON_CHESTPLATE).setDisplayName("§9Unbreaking"));
            }
            },
            new List<isItem>(x -> true)
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.OXYGEN, new BetterItem(Material.LEATHER_HELMET).setDisplayName("§9Respiration"));
                put(Enchantment.WATER_WORKER, new BetterItem(Material.DIAMOND_HELMET).setDisplayName("§9Aqua Affinity"));
            }},
            new List<isItem>(x ->
                    x.getType() == Material.DIAMOND_HELMET || x.getType() == Material.IRON_HELMET || x.getType() == Material.GOLD_HELMET || x.getType() == Material.LEATHER_HELMET || x.getType() == Material.CHAINMAIL_HELMET
            )
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.PROTECTION_ENVIRONMENTAL, new BetterItem(Material.MONSTER_EGG).setDisplayName("§9Protection"));
                put(Enchantment.PROTECTION_EXPLOSIONS, new BetterItem(Material.TNT).setDisplayName("§9Blast Protection"));
                put(Enchantment.PROTECTION_FIRE, new BetterItem(Material.FIREBALL).setDisplayName("§9Fire Protection"));
                put(Enchantment.PROTECTION_PROJECTILE, new BetterItem(Material.ARROW).setDisplayName("§9Projectile Protection"));
                put(Enchantment.THORNS, new BetterItem(Material.FIREWORK).setDisplayName("§9Thorns"));
            }},
            new List<isItem>(x ->
                    x.getType() == Material.DIAMOND_CHESTPLATE || x.getType() == Material.IRON_CHESTPLATE || x.getType() == Material.GOLD_CHESTPLATE || x.getType() == Material.LEATHER_CHESTPLATE || x.getType() == Material.CHAINMAIL_CHESTPLATE ||
                    x.getType() == Material.DIAMOND_HELMET || x.getType() == Material.IRON_HELMET || x.getType() == Material.GOLD_HELMET || x.getType() == Material.LEATHER_HELMET || x.getType() == Material.CHAINMAIL_HELMET ||
                    x.getType() == Material.DIAMOND_LEGGINGS || x.getType() == Material.IRON_LEGGINGS || x.getType() == Material.GOLD_LEGGINGS || x.getType() == Material.LEATHER_LEGGINGS || x.getType() == Material.CHAINMAIL_LEGGINGS ||
                    x.getType() == Material.DIAMOND_BOOTS || x.getType() == Material.IRON_BOOTS || x.getType() == Material.GOLD_BOOTS || x.getType() == Material.LEATHER_BOOTS || x.getType() == Material.CHAINMAIL_BOOTS
            )
        );
        new EnchantmentCategory(
            new HashMap<Enchantment, ItemStack>(){
            {
                put(Enchantment.PROTECTION_FALL, new BetterItem(Material.FEATHER).setDisplayName("§9Feather Falling"));
                put(Enchantment.DEPTH_STRIDER, new BetterItem(Material.WATER_BUCKET).setDisplayName("§9Depth Strider"));
            }},
            new List<isItem>(x ->
                    x.getType() == Material.DIAMOND_BOOTS || x.getType() == Material.IRON_BOOTS || x.getType() == Material.GOLD_BOOTS || x.getType() == Material.LEATHER_BOOTS || x.getType() == Material.CHAINMAIL_BOOTS
            )
        );
    }
}
