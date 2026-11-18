package dev.test.snakegame.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.test.snakegame.SnakeGameMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class ClientKeys {
    public static final KeyMapping OPEN_SNAKE = new KeyMapping(
            "key.snakegame.open",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.snakegame"
    );

    private ClientKeys() {
    }

    @Mod.EventBusSubscriber(modid = SnakeGameMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(OPEN_SNAKE);
        }
    }

    @Mod.EventBusSubscriber(modid = SnakeGameMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBusEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft minecraft = Minecraft.getInstance();
                while (OPEN_SNAKE.consumeClick()) {
                    if (minecraft.screen == null) {
                        minecraft.setScreen(new SnakeScreen());
                    }
                }
            }
        }
    }
}
