package mcsoc.wildlife.gameeffects

import mcsoc.wildlife.datatrackers.eventScheduler
import mcsoc.wildlife.datatrackers.gameState
import mcsoc.wildlife.datatrackers.playerData
import mcsoc.wildlife.datatrackers.playerdata.getActivePlayers
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.GameType
import kotlin.time.Duration.Companion.seconds


fun ServerLevel.startCountdown(cardId: String): Int {
    if (level.gameState.phase.isRunning) endActiveCard()
    
    // call interface/initialise

    this.getActivePlayers().forEach { it.onStartCount() }

    this.gameState.startCountdown(cardId)
    this.eventScheduler.queueGameStartCounter(5.seconds)
    return 0
}


fun ServerLevel.startActiveCard() {
    this.getActivePlayers().forEach { it.onStartGame() }
    
    // call interface/start
    
    this.gameState.startGame()
}

fun ServerLevel.endActiveCard() {
    // call interface/end
    
    this.getActivePlayers().forEach { it.onFinish() }

    this.gameState.endGame()
        
    // call interface/terminate

}

fun ServerLevel.tickGame() {
    if (this.gameState.phase.isActive) {
        this.getActivePlayers().forEach { it.tickPlayer() }
        this.playerData.activePlayerData.forEach { it.tickTimer() }
    }
}


// Player specific game functions

private fun ServerPlayer.onStartCount() {
    // this.setGameMode(GameType.SPECTATOR)
}

private fun ServerPlayer.onStartGame() {
    this.setGameMode(GameType.SURVIVAL)
}

private fun ServerPlayer.tickPlayer() {

}

private fun ServerPlayer.onFinish() {
    this.setGameMode(GameType.SPECTATOR)
}

