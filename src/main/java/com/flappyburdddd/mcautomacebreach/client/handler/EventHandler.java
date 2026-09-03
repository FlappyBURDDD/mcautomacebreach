package com.flappyburdddd.mcautomacebreach.client.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flappyburdddd.mcautomacebreach.client.config.ModConfig;
import com.flappyburdddd.mcautomacebreach.client.McAutomaceBreachClient;
import com.flappyburdddd.mcautomacebreach.client.util.InventoryUtil;

public class EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger("mcautomacebreach");
    
    private static int lastBreachSwapTick = 0;
    private static int lastShieldDrainTick = 0;
    private static boolean isProcessingBreachSwap = false;
    
    public static void onClientTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;
        
        // Check for Stunslamming toggle
        while (McAutomaceBreachClient.stunSlamToggleKey.wasPressed()) {
            ModConfig.configData.autoStunSlamEnabled = !ModConfig.configData.autoStunSlamEnabled;
            ModConfig.saveConfig();
            player.sendMessage(
                net.minecraft.text.Text.of("[McAutomaceBreach] Auto Stunslam: " + (ModConfig.configData.autoStunSlamEnabled ? "§aON" : "§cOFF")),
                true
            );
        }
    }
    
    /**
     * Called when player attacks with sword for AutoBreachSwap
     */
    public static void onPlayerAttack(MinecraftClient client) {
        if (!ModConfig.configData.autoBreachSwapEnabled) return;
        if (isProcessingBreachSwap) return;
        
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        
        ItemStack mainHandItem = player.getMainHandStack();
        
        // Check if player is holding sword
        if (!InventoryUtil.isHoldingSword(mainHandItem)) return;
        
        // Find best breach mace
        int breachMaceSlot = InventoryUtil.findBestBreachMaceSlot(player.getInventory());
        if (breachMaceSlot == -1) return;
        
        // Perform breach swap
        performBreachSwap(player, breachMaceSlot);
    }
    
    /**
     * Performs the breach swap sequence
     */
    private static void performBreachSwap(ClientPlayerEntity player, int maceSlot) {
        isProcessingBreachSwap = true;
        int currentSlot = player.getInventory().selectedSlot;
        
        // Switch to mace
        player.getInventory().selectedSlot = maceSlot;
        
        // Swing the mace (attack)
        player.swingHand(Hand.MAIN_HAND);
        
        // Schedule return to sword after attack completes
        new Thread(() -> {
            try {
                Thread.sleep(500); // Wait for attack animation
                player.getInventory().selectedSlot = currentSlot;
                isProcessingBreachSwap = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Called when player falls (for Stunslam detection)
     */
    public static void onPlayerFall(ClientPlayerEntity player) {
        if (!ModConfig.configData.autoStunSlamEnabled) return;
        
        // Check if player is blocking with shield
        if (!player.isBlocking()) return;
        
        // Find best density mace
        int densityMaceSlot = InventoryUtil.findBestDensityMaceSlot(player.getInventory());
        if (densityMaceSlot == -1) return;
        
        // TODO: Break shield with axe, then hit with mace
        LOGGER.debug("[McAutomaceBreach] Stunslam condition detected, executing sequence...");
    }
    
    /**
     * Called when player hits shield with mace (for Shield Drain)
     */
    public static void onShieldHit(ClientPlayerEntity player) {
        if (!ModConfig.configData.shieldDrainEnabled) return;
        
        ItemStack mainHandItem = player.getMainHandStack();
        if (!InventoryUtil.isDensityMace(mainHandItem)) return;
        
        // Schedule next shield drain hit
        lastShieldDrainTick = 0;
    }
}
