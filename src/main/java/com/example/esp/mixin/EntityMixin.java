package com.example.esp.mixin;

import com.example.esp.EspClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    // Lässt andere Spieler clientseitig "leuchten" -> Umriss ist durch Wände sichtbar
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void esp$glow(CallbackInfoReturnable<Boolean> cir) {
        if (EspClient.enabled
                && (Object) this instanceof PlayerEntity p
                && p != MinecraftClient.getInstance().player) {
            cir.setReturnValue(true);
        }
    }
}
