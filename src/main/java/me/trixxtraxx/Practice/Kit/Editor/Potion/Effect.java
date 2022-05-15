package me.trixxtraxx.Practice.Kit.Editor.Potion;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public enum Effect
{
    BLINDNESS(PotionEffectType.BLINDNESS, 0, "Blindness", "§cBlindness {Amplifier} ({Duration})", Material.WOOL, (byte)15),
    RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE, 0, "Resistance", "§7Resistance {Amplifier} ({Duration})", Material.DIAMOND_CHESTPLATE),
    HASTE(PotionEffectType.FAST_DIGGING, 0, "Haste", "§7Haste {Amplifier} ({Duration})", Material.GOLD_PICKAXE),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE, 8227, "Fire Resistance", "§7Fire Resistance {Amplifier} ({Duration})", Material.LAVA_BUCKET),
    HEAL(PotionEffectType.HEAL, 8261, "Heal", "§7Heal {Amplifier} ({Duration})", Material.SPECKLED_MELON),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE, 8201, "Strength", "§7Strength {Amplifier} ({Duration})", Material.DIAMOND_SWORD),
    INVISIBILITY(PotionEffectType.INVISIBILITY, 8238, "Invisibility", "§7Invisibility {Amplifier} ({Duration})",Material.GOLDEN_CARROT),
    JUMP_BOOST(PotionEffectType.JUMP, 8203, "Jump Boost", "§7Jump Boost {Amplifier} ({Duration})", Material.RABBIT_FOOT),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION, 8230, "Night Vision", "§7Night Vision {Amplifier} ({Duration})", Material.STAINED_GLASS, (short) 5),
    POISON(PotionEffectType.POISON, 8196, "Poison", "§cPoison {Amplifier} ({Duration})", Material.POISONOUS_POTATO),
    REGENERATION(PotionEffectType.REGENERATION, 8193, "Regeneration", "§7Regeneration {Amplifier} ({Duration})", Material.GOLDEN_APPLE),
    SLOW(PotionEffectType.SLOW, 8234, "Slow", "§7Slow {Amplifier} ({Duration})", Material.OBSIDIAN),
    SPEED(PotionEffectType.SPEED, 8194, "Speed", "§7Speed {Amplifier} ({Duration})", Material.FEATHER),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING, 8237, "Water Breathing", "§7Water Breathing {Amplifier} ({Duration})", Material.WATER_BUCKET),
    WEAKNESS(PotionEffectType.WEAKNESS, 8232, "Weakness", "§cWeakness {Amplifier} ({Duration})", Material.WOOD_SWORD, (short) 1),
    WITHER(PotionEffectType.WITHER, 0, "Wither", "§cWither {Amplifier} ({Duration})", Material.SKULL, (short) 1);

    private PotionEffectType type;
    private BetterItem item;
    private String displayName;
    private String description;
    private int id;

    Effect(PotionEffectType type, int id, String displayName, String description, Material mat)
    {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.id = id;
        this.item = new BetterItem(mat, 1).setDisplayName(displayName);
    }
    
    Effect(PotionEffectType type, int id, String displayName, String description, Material mat, short data)
    {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.id = id;
        this.item = new BetterItem(mat, 1, data).setDisplayName(displayName);
    }

    public PotionEffectType getType(){
        return type;
    }

    public BetterItem getItem(){
        return item;
    }
    
    public String getName(){
        return displayName;
    }
    
    public String getDescription(){
        return description;
    }
    
    public int getId(){
        return id;
    }
}
