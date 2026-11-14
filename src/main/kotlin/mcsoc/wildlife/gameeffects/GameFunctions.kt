package mcsoc.wildlife.gameeffects

import mcsoc.wildlife.datatrackers.eventScheduler
import mcsoc.wildlife.datatrackers.gameState
import mcsoc.wildlife.datatrackers.playerData
import mcsoc.wildlife.datatrackers.playerdata.getActivePlayers
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.GameType
import kotlin.time.Duration.Companion.seconds


fun ServerLevel.startCountdown(mapId: String): Int {
    if (this.gameState.phase.isRunning) {
        this.end()
    }

    this.getActivePlayers().forEach { it.onStartCount() }

    this.gameState.startCountdown()
    this.eventScheduler.queueGameStartCounter(5.seconds)
    return 0
}


fun ServerLevel.startGame() {
    for (player in playerData.activePlayers) {
        val playerData = playerData.getPlayerData(player).also { it.resetData() }
    }

    this.getActivePlayers().forEach { it.onStartGame() }

    this.gameState.startGame()
}

fun ServerLevel.tickGame() {
    if (this.gameState.phase.isActive) {
        this.getActivePlayers().forEach { it.tickPlayer() }
        this.playerData.activePlayerData.forEach { it.tickTimer() }
    }
}

fun ServerLevel.end() {
    this.getActivePlayers().forEach { it.onFinish() }
    
    // clear data
    this.playerData.resetData()
    this.gameState.end()
}


// Player specific game functions

private fun ServerPlayer.onStartCount() {
    this.setGameMode(GameType.SPECTATOR)
}

private fun ServerPlayer.onStartGame() {
    this.setGameMode(GameType.SURVIVAL)
}

private fun ServerPlayer.tickPlayer() {

}

private fun ServerPlayer.onFinish() {
    this.setGameMode(GameType.SPECTATOR)
}

