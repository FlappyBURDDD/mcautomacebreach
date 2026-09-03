package com.flappyburdddd.mcautomacebreach.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.flappyburdddd.mcautomacebreach.client.handler.EventHandler;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    
    @Inject(method = "swingHand", at = @At("HEAD"))
    private void onClientSwingHand(Hand hand, CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        EventHandler.onPlayerAttack(player.getClient());
    }
}
