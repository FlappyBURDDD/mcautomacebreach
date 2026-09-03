package com.flappyburdddd.mcautomacebreach.client.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;

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
     * Checks if item has Breach enchantment (custom enchantment check)
     */
    public static boolean hasBreachEnchantment(ItemStack stack) {
        // In vanilla, we check for custom name or NBT data
        // For this implementation, we assume "Breach" items have specific enchantments
        // You can customize this based on your server's Breach Mace definition
        return stack.hasCustomName() && stack.getCustomName().getString().toLowerCase().contains("breach");
    }
    
    /**
     * Checks if item has Density enchantment
     */
    public static boolean hasDensityEnchantment(ItemStack stack) {
        return stack.hasCustomName() && stack.getCustomName().getString().toLowerCase().contains("density");
    }
    
    /**
     * Gets the enchantment level of Breach enchantment
     */
    public static int getBreachLevel(ItemStack stack) {
        // Custom logic to determine breach level
        // This can be extended based on server implementation
        return 1; // Default level
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
     * Checks if player is holding a sword
     */
    public static boolean isHoldingSword(ItemStack stack) {
        return stack.getItem().toString().toLowerCase().contains("sword");
    }
}
