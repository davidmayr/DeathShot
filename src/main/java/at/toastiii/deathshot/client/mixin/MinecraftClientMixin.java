package at.toastiii.deathshot.client.mixin;

import at.toastiii.deathshot.client.DeathShotClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public Screen currentScreen;
    private boolean tookDeathShot;

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null)
            return;
        if(!tookDeathShot && player.isDead()) {
            if(player.showsDeathScreen() && !(currentScreen instanceof DeathScreen))
                return;
            tookDeathShot = true;
            DeathShotClient.saveScreenShot();
        } else if(tookDeathShot && !player.isDead()) {
            tookDeathShot = false;
        }
    }
}
