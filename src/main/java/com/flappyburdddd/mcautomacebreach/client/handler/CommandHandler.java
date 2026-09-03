package com.flappyburdddd.mcautomacebreach.client.handler;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandBuildContext;

import com.flappyburdddd.mcautomacebreach.client.screen.SettingsScreen;

public class CommandHandler {
    
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext) -> {
            registerHelbCommand(dispatcher);
        });
    }
    
    private static void registerHelbCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            com.mojang.brigadier.builder.LiteralArgumentBuilder.<FabricClientCommandSource>literal("helb")
                .executes(context -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.setScreen(new SettingsScreen());
                    return 1;
                })
        );
    }
}
