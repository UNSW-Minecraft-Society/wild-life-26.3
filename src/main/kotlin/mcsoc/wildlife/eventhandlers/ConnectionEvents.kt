package mcsoc.wildlife.eventhandlers

import mcsoc.wildlife.datatrackers.playerData
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents


fun registerPlayerConnectionEvents() {
    ServerPlayerEvents.JOIN.register{player ->
        player.level().playerData.let{
            it.getPlayerData(player)
            it.activePlayers.add(player.uuid)
        }
    }
}