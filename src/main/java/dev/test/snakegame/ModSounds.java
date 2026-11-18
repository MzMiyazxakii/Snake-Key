package dev.test.snakegame;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SnakeGameMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DEATH =
            SOUND_EVENTS.register("death", () ->
                    SoundEvent.createVariableRangeEvent(new ResourceLocation(SnakeGameMod.MOD_ID, "death")));

    private ModSounds() {}

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}