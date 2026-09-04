package com.flappyburdddd.mcautomacebreach.client.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flappyburdddd.mcautomacebreach.client.config.ModConfig;
import com.flappyburdddd.mcautomacebreach.client.McAutomaceBreachClient;
import com.flappyburdddd.mcautomacebreach.client.util.InventoryUtil;

public class EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger("mcautomacebreach");
    
    private static int breachSwapCooldown = 0;
    private static int shieldDrainTickCounter = 0;
    private static int lastShieldDrainTick = 0;
    private static boolean isProcessingBreachSwap = false;
    private static boolean wasBlocking = false;
    private static float lastFallDistance = 0;
    
    // Stunslam configuration
    private static final int MACE_ATTACK_COOLDOWN = 17; // Ticks
    
    public static void onClientTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;
        
        // Check for Stunslamming toggle
        while (McAutomaceBreachClient.stunSlamToggleKey.wasPressed()) {
            ModConfig.configData.autoStunSlamEnabled = !ModConfig.configData.autoStunSlamEnabled;
            ModConfig.saveConfig();
            player.sendMessage(
                net.minecraft.text.Text.of("§6[McAutomaceBreach] Auto Stunslam: " + (ModConfig.configData.autoStunSlamEnabled ? "§aON" : "§cOFF")),
                true
            );
        }
        
        // Decrement cooldowns
        if (breachSwapCooldown > 0) breachSwapCooldown--;
        if (shieldDrainTickCounter > 0) shieldDrainTickCounter--;
        
        // Handle Stunslam detection
        handleStunSlamDetection(player);
        
        // Handle Shield Drain
        if (ModConfig.configData.shieldDrainEnabled) {
            handleShieldDrain(player);
        }
    }
    
    /**
     * Called when player attacks with sword for AutoBreachSwap
     */
    public static void onPlayerAttack(MinecraftClient client) {
        if (!ModConfig.configData.autoBreachSwapEnabled) return;
        if (isProcessingBreachSwap) return;
        if (breachSwapCooldown > 0) return;
        
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
        
        // Set cooldown
        breachSwapCooldown = MACE_ATTACK_COOLDOWN;
        
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
        
        LOGGER.debug("[McAutomaceBreach] BreachSwap executed: Mace slot " + maceSlot + " -> " + currentSlot);
    }
    
    /**
     * Detects Stunslam conditions: player blocking + falling
     */
    private static void handleStunSlamDetection(ClientPlayerEntity player) {
        if (!ModConfig.configData.autoStunSlamEnabled) return;
        
        boolean isCurrentlyBlocking = player.isBlocking();
        float currentFallDistance = player.fallDistance;
        
        // Detect transition from blocking to falling
        if (wasBlocking && isCurrentlyBlocking) {
            // Player is falling while blocking
            if (currentFallDistance > 0.5f && currentFallDistance > lastFallDistance) {
                // Still falling
                lastFallDistance = currentFallDistance;
                return;
            } else if (currentFallDistance >= 0.5f && lastFallDistance > 0) {
                // About to hit ground - execute stunslam
                executeStunslam(player);
                wasBlocking = false;
                lastFallDistance = 0;
            }
        }
        
        wasBlocking = isCurrentlyBlocking;
        lastFallDistance = currentFallDistance;
    }
    
    /**
     * Executes the Stunslam sequence
     */
    private static void executeStunslam(ClientPlayerEntity player) {
        // Find best density mace
        int densityMaceSlot = InventoryUtil.findBestDensityMaceSlot(player.getInventory());
        if (densityMaceSlot == -1) {
            LOGGER.debug("[McAutomaceBreach] No Density Mace found for Stunslam!");
            return;
        }
        
        int currentSlot = player.getInventory().selectedSlot;
        
        // Switch to density mace
        player.getInventory().selectedSlot = densityMaceSlot;
        
        // Swing mace for stunslam hit
        player.swingHand(Hand.MAIN_HAND);
        
        // Schedule return to previous item
        new Thread(() -> {
            try {
                Thread.sleep(300);
                player.getInventory().selectedSlot = currentSlot;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        LOGGER.debug("[McAutomaceBreach] Stunslam executed!");
    }
    
    /**
     * Handles Shield Drain - repeatedly hits shields with Density Mace
     */
    private static void handleShieldDrain(ClientPlayerEntity player) {
        ItemStack mainHandItem = player.getMainHandStack();
        
        // Check if holding density mace
        if (!InventoryUtil.isDensityMace(mainHandItem)) return;
        
        // Increment tick counter
        shieldDrainTickCounter++;
        
        // Execute shield drain hit at configured intervals
        if (shieldDrainTickCounter >= ModConfig.configData.shieldDrainDelayTicks) {
            player.swingHand(Hand.MAIN_HAND);
            shieldDrainTickCounter = 0;
            LOGGER.debug("[McAutomaceBreach] Shield Drain hit executed");
        }
    }
    
    /**
     * Called when player hits shield with mace (for Shield Drain)
     */
    public static void onShieldHit(ClientPlayerEntity player) {
        if (!ModConfig.configData.shieldDrainEnabled) return;
        
        ItemStack mainHandItem = player.getMainHandStack();
        if (!InventoryUtil.isDensityMace(mainHandItem)) return;
        
        // Reset shield drain tick counter
        shieldDrainTickCounter = 0;
    }
}
