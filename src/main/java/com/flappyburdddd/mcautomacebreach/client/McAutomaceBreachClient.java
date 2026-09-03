package com.flappyburdddd.mcautomacebreach.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flappyburdddd.mcautomacebreach.McAutomaceBreachMod;
import com.flappyburdddd.mcautomacebreach.client.config.ModConfig;
import com.flappyburdddd.mcautomacebreach.client.handler.EventHandler;
import com.flappyburdddd.mcautomacebreach.client.handler.CommandHandler;

@Environment(EnvType.CLIENT)
public class McAutomaceBreachClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(McAutomaceBreachMod.MOD_ID);
    
    public static KeyBinding stunSlamToggleKey;
    public static KeyBinding guiToggleKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[McAutomaceBreach Client] Initializing Client...");
        
        // Load config
        ModConfig.loadConfig();
        
        // Register keybindings
        registerKeybindings();
        
        // Register event handlers
        ClientTickEvents.END_CLIENT_TICK.register(client -> EventHandler.onClientTick(client));
        
        // Register command handler
        CommandHandler.registerCommands();
        
        LOGGER.info("[McAutomaceBreach Client] Client initialized successfully!");
    }
    
    private void registerKeybindings() {
        stunSlamToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.mcautomacebreach.stunslamtoggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "category.mcautomacebreach"
        ));
        
        guiToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.mcautomacebreach.guitoggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.mcautomacebreach"
        ));
    }
}
