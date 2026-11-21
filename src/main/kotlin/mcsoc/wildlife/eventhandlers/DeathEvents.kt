package mcsoc.wildlife.eventhandlers

import mcsoc.wildlife.datatrackers.playerData
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.server.level.ServerPlayer

fun registerPlayerDeathEvent() {
    ServerLivingEntityEvents.AFTER_DEATH.register{entity, source ->
        if (entity !is ServerPlayer) return@register
        val level = entity.level()
        level.playerData.getPlayerData(entity).let{
            val next = it.lifestate.next ?: return@let
            it.lifestate = next
        }
        
        if (source.entity is ServerPlayer) {
             // TODO handle killer 
        }
    }
}
