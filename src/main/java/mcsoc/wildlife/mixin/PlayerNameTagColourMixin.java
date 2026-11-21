package mcsoc.wildlife.mixin;

import mcsoc.wildlife.datatrackers.LevelData;
import mcsoc.wildlife.datatrackers.playerdata.LifeState;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;


@Mixin(ServerPlayer.class)
public class PlayerNameTagColourMixin extends EntityNameTagMixin {

    @Override
    void getNameTagDisplayName(CallbackInfoReturnable<Component> cir) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        MutableComponent name = (MutableComponent) player.getName();
        LifeState lifestate = LevelData.getPlayerData(player.level()).getPlayerData(player).getLifestate();

        // cir.setReturnValue(name.withStyle(lifestate.getColour()));
        cir.setReturnValue(name);
    }
}
