package com.example.esp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class EspClient implements ClientModInitializer {
    public static boolean enabled = true;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.esp.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "category.esp"));

        // V drücken = ESP an/aus
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
            }
        });

        // Spielerliste oben rechts
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (!enabled || mc.player == null || mc.world == null) return;

            int screenW = mc.getWindow().getScaledWidth();
            int y = 5;
            String title = "Spieler in der Nähe";
            ctx.drawTextWithShadow(mc.textRenderer, title,
                    screenW - mc.textRenderer.getWidth(title) - 5, y, 0xFFFFFF55);
            y += 12;

            for (PlayerEntity p : mc.world.getPlayers()) {
                if (p == mc.player) continue;
                String line = p.getName().getString() + " [" + Math.round(mc.player.distanceTo(p)) + "m]";
                ctx.drawTextWithShadow(mc.textRenderer, line,
                        screenW - mc.textRenderer.getWidth(line) - 5, y, 0xFFFFFFFF);
                y += 10;
            }
        });
    }
}
