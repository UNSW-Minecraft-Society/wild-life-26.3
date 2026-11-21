package mcsoc.wildlife.gameeffects

import mcsoc.wildlife.datatrackers.eventScheduler
import mcsoc.wildlife.datatrackers.gameClock
import mcsoc.wildlife.datatrackers.gameState
import mcsoc.wildlife.datatrackers.gamedata.Wildcard
import mcsoc.wildlife.datatrackers.playerData
import mcsoc.wildlife.datatrackers.playerdata.LifeState
import mcsoc.wildlife.datatrackers.playerdata.getActivePlayers
import mcsoc.wildlife.datatrackers.scoreboardDisplay
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType
import net.minecraft.world.scores.DisplaySlot
import kotlin.time.Duration.Companion.seconds


const val PLAYER_SCORES_SCOREBOARD = "scores"

/* 0 means success
 * 1 means card not found
 */
fun ServerLevel.startCountdown(cardId: String): Int {
    if (level.gameState.phase.isRunning) endCurrentRound()
    val card = Wildcard.fromNamespace(cardId) ?: return 1
    
    this.scoreboardDisplay.tryAddObjective(
        PLAYER_SCORES_SCOREBOARD, 
        Component.literal("Round Scores")
    )

    // TODO make this not evil pls 
    this.playerData.activePlayers.addAll(this.server.playerList.players.map(Player::getUUID))
    
    this.getActivePlayers().forEach {
        this.scoreboardDisplay.addPlayerToTracking(it.uuid, PLAYER_SCORES_SCOREBOARD, setOf(DisplaySlot.SIDEBAR))
        it.onStartCount() 
    }
    this.gameState.startCountdown(card)
    this.eventScheduler.queueGameStartCounter(5.seconds)
    return 0
}


fun ServerLevel.startActiveCard() {
    this.getActivePlayers().forEach { it.onStartGame() }
    this.gameState.startGame()
}

fun ServerLevel.endCurrentRound(): Int {    
    if (!this.gameState.phase.isRunning) return 1
    
    this.gameState.endGame()
    this.getActivePlayers().forEach { it.onFinish() }
    this.playerData.onRoundEnd()
    this.gameState.terminate()
    return 0
}


fun ServerLevel.tickGame() {
    if (this.gameState.phase.isActive) {
        this.getActivePlayers().forEach { it.tickPlayer() }
        // per second
        for (i in 0u..this.gameClock.timerSecond) {
            this.playerData.activePlayerData
                .filter{ it.lifestate == LifeState.Green }
                .forEach{ 
                    it.scorePoints(1)
                }
        }
        
        this.playerData.activePlayerData.forEach {
            this.scoreboardDisplay.setScore(
                PLAYER_SCORES_SCOREBOARD, it.playerName, 
                it.pointsThisRound, null
            )
            it.tickTimer() 
        }
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
    val playerData = this.level().playerData.getPlayerData(this)
}

