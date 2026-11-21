package mcsoc.wildlife.mixin;

import javax.swing.text.html.parser.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.Component;


@Mixin(Entity.class)
public class EntityNameTagMixin {
    
    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    void getNameTagDisplayName(CallbackInfoReturnable<Component> cir) {}
}
