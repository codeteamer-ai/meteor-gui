package com.meteorui.client.mixin;

import com.meteorui.client.module.Module;
import com.meteorui.client.module.ModuleManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fullbright mixin for 1.21.11's shader-based lightmap.
 *
 * In 1.21.11, the lightmap is computed on the GPU via lightmap.fsh.
 * update() packs parameters into a UBO. We redirect three method calls
 * to force the NightVisionFactor uniform to 1.0 and zero out Darkness:
 *
 * 1. hasStatusEffect(NIGHT_VISION) → true   (enter NV code path)
 * 2. getNightVisionStrength()      → 1.0f   (full NV, bypasses real effect)
 * 3. getDarkness()                 → 0.0f   (prevent Darkness re-darkening)
 */
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {

    @Shadow
    private float getDarkness(LivingEntity entity, float darknessEffect, float delta) {
        throw new AssertionError("Mixin shadow");
    }

    private boolean isFullbrightActive() {
        Module fb = ModuleManager.getInstance().getModule("Fullbright");
        return fb != null && fb.isEnabled();
    }

    @Redirect(
        method = "update",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z",
                 ordinal = 0)
    )
    private boolean meteorGui$forceHasNightVision(ClientPlayerEntity player, RegistryEntry<StatusEffect> effect) {
        if (isFullbrightActive()) return true;
        return player.hasStatusEffect(effect);
    }

    @Redirect(
        method = "update",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/client/render/GameRenderer;getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F")
    )
    private float meteorGui$forceNightVisionStrength(LivingEntity entity, float tickDelta) {
        if (isFullbrightActive()) return 1.0f;
        return GameRenderer.getNightVisionStrength(entity, tickDelta);
    }

    @Redirect(
        method = "update",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/client/render/LightmapTextureManager;getDarkness(Lnet/minecraft/entity/LivingEntity;FF)F")
    )
    private float meteorGui$zeroDarkness(LightmapTextureManager self, LivingEntity entity, float darknessEffect, float delta) {
        if (isFullbrightActive()) return 0.0f;
        return getDarkness(entity, darknessEffect, delta);
    }
}
