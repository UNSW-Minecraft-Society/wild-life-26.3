package mcsoc.wildlife.gameeffects

import mcsoc.wildlife.datatrackers.eventScheduler
import mcsoc.wildlife.datatrackers.gameState
import mcsoc.wildlife.datatrackers.gamedata.Wildcard
import mcsoc.wildlife.datatrackers.playerData
import mcsoc.wildlife.datatrackers.playerdata.getActivePlayers
import mcsoc.wildlife.utils.runFunction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType
import kotlin.time.Duration.Companion.seconds

/* 0 means success
 * 1 means card not found
 */
fun ServerLevel.startCountdown(cardId: String): Int {
    if (level.gameState.phase.isRunning) endCurrentRound()
    val card = Wildcard.fromNamespace(cardId) ?: return 1

    // TODO make this not evil pls 
    this.playerData.activePlayers.addAll(this.server.playerList.players.map(Player::getUUID))
    
    this.getActivePlayers().forEach { it.onStartCount() }
    this.playerData.newRound()
    this.gameState.startCountdown(card)
    this.eventScheduler.queueGameStartCounter(5.seconds)
    return 0
}


fun ServerLevel.startActiveCard() {
    this.getActivePlayers().forEach { it.onStartGame() }
    this.gameState.startGame()
    runFunction("wildlife", "general/showtitle", this.server)
}

fun ServerLevel.endCurrentRound(): Int {    
    if (!this.gameState.phase.isRunning) return 1
    
    this.gameState.endGame()
    this.getActivePlayers().forEach { it.onFinish() }
    this.playerData.newRound()
    this.gameState.terminate()
    return 0
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

