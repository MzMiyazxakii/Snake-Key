package dev.test.snakegame;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SnakeGameMod.MOD_ID)
public class SnakeGameMod {
    public static final String MOD_ID = "snakegame";

    public SnakeGameMod() {
        ModSounds.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}