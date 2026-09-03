package com.flappyburdddd.mcautomacebreach.client.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

public class InventoryUtil {
    
    /**
     * Checks if an item is a Breach Mace
     */
    public static boolean isBreachMace(ItemStack stack) {
        return stack.getItem() == Items.MACE && hasBreachEnchantment(stack);
    }
    
    /**
     * Checks if an item is a Density Mace (for Stunslam and Shield Drain)
     */
    public static boolean isDensityMace(ItemStack stack) {
        return stack.getItem() == Items.MACE && hasDensityEnchantment(stack);
    }
    
    /**
     * Checks if item has Breach enchantment via NBT tag
     */
    public static boolean hasBreachEnchantment(ItemStack stack) {
        if (!stack.hasNbt()) return false;
        
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return false;
        
        // Check custom name for "breach" keyword
        if (nbt.contains("display")) {
            NbtCompound display = nbt.getCompound("display");
            if (display.contains("Name")) {
                String name = display.getString("Name");
                // Parse JSON text (Minecraft format)
                if (name.toLowerCase().contains("breach")) {
                    return true;
                }
            }
        }
        
        // Check enchantments NBT
        if (nbt.contains("Enchantments")) {
            NbtList enchantments = nbt.getList("Enchantments", 10); // 10 = compound tag type
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchant = enchantments.getCompound(i);
                String id = enchant.getString("id");
                if (id.toLowerCase().contains("breach")) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if item has Density enchantment via NBT tag
     */
    public static boolean hasDensityEnchantment(ItemStack stack) {
        if (!stack.hasNbt()) return false;
        
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return false;
        
        // Check custom name for "density" keyword
        if (nbt.contains("display")) {
            NbtCompound display = nbt.getCompound("display");
            if (display.contains("Name")) {
                String name = display.getString("Name");
                if (name.toLowerCase().contains("density")) {
                    return true;
                }
            }
        }
        
        // Check enchantments NBT
        if (nbt.contains("Enchantments")) {
            NbtList enchantments = nbt.getList("Enchantments", 10);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchant = enchantments.getCompound(i);
                String id = enchant.getString("id");
                if (id.toLowerCase().contains("density")) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Gets the Breach enchantment level from NBT
     */
    public static int getBreachLevel(ItemStack stack) {
        if (!stack.hasNbt()) return 0;
        
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return 0;
        
        if (nbt.contains("Enchantments")) {
            NbtList enchantments = nbt.getList("Enchantments", 10);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchant = enchantments.getCompound(i);
                String id = enchant.getString("id");
                if (id.toLowerCase().contains("breach")) {
                    return enchant.getShort("lvl");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Gets the Density enchantment level from NBT
     */
    public static int getDensityLevel(ItemStack stack) {
        if (!stack.hasNbt()) return 0;
        
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return 0;
        
        if (nbt.contains("Enchantments")) {
            NbtList enchantments = nbt.getList("Enchantments", 10);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchant = enchantments.getCompound(i);
                String id = enchant.getString("id");
                if (id.toLowerCase().contains("density")) {
                    return enchant.getShort("lvl");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Finds the best Breach Mace in hotbar (highest enchantment level)
     */
    public static int findBestBreachMaceSlot(PlayerInventory inventory) {
        int bestSlot = -1;
        int bestLevel = -1;
        
        // Search only hotbar (slots 0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (isBreachMace(stack)) {
                int level = getBreachLevel(stack);
                if (level > bestLevel) {
                    bestLevel = level;
                    bestSlot = i;
                }
            }
        }
        
        return bestSlot;
    }
    
    /**
     * Finds the best Density Mace in hotbar (highest enchantment level)
     */
    public static int findBestDensityMaceSlot(PlayerInventory inventory) {
        int bestSlot = -1;
        int bestLevel = -1;
        
        // Search only hotbar (slots 0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (isDensityMace(stack)) {
                int level = getDensityLevel(stack);
                if (level > bestLevel) {
                    bestLevel = level;
                    bestSlot = i;
                }
            }
        }
        
        return bestSlot;
    }
    
    /**
     * Checks if player is holding a sword
     */
    public static boolean isHoldingSword(ItemStack stack) {
        return stack.getItem().toString().toLowerCase().contains("sword");
    }
    
    /**
     * Checks if player is holding a shield
     */
    public static boolean isHoldingShield(ItemStack stack) {
        return stack.getItem() == Items.SHIELD;
    }
}
