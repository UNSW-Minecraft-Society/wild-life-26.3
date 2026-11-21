package mcsoc.wildlife.eventhandlers

import mcsoc.wildlife.datatrackers.playerData
import mcsoc.wildlife.datatrackers.scoreboardDisplay
import mcsoc.wildlife.gameeffects.PLAYER_SCORES_SCOREBOARD
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.world.scores.DisplaySlot


fun registerPlayerConnectionEvents() {
    ServerPlayerEvents.JOIN.register{player ->
        val level = player.level()
        level.playerData.let{
            it.getPlayerData(player)
            it.activePlayers.add(player.uuid)
        }
        
        level.scoreboardDisplay.addPlayerToTracking(
            player.uuid, PLAYER_SCORES_SCOREBOARD, 
            setOf(DisplaySlot.SIDEBAR)
        )
    }
}