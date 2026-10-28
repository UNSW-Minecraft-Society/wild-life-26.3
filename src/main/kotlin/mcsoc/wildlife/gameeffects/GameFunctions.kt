package mcsoc.wildlife.gameeffects

import mcsoc.wildlife.datatrackers.eventsQueue
import mcsoc.wildlife.datatrackers.gameData
import mcsoc.wildlife.datatrackers.playerData
import mcsoc.wildlife.datatrackers.playerdata.getActivePlayers
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.GameType
import kotlin.time.Duration.Companion.seconds


fun ServerLevel.startCountdown(mapId: String): Int {
    if (this.gameData.phase.isRunning) {
        this.end()
    }

    this.getActivePlayers().forEach { it.onStartCount() }

    this.gameData.startCountdown()
    this.eventsQueue.queueGameStartCounter(5.seconds)
    return 0
}


fun ServerLevel.startGame() {
    for (player in playerData.activePlayers) {
        val playerData = playerData.getPlayerData(player).also { it.resetData() }
    }

    this.getActivePlayers().forEach { it.onStartGame() }

    this.gameData.startGame()
}

fun ServerLevel.tickActivePlayers() {
    if (this.gameData.phase.isActive) {
        this.getActivePlayers().forEach { it.tick() }
    }
}

fun ServerLevel.end() {
    this.getActivePlayers().forEach { it.onFinish() }
    
    // clear data
    this.playerData.resetData()
    this.gameData.end()
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

