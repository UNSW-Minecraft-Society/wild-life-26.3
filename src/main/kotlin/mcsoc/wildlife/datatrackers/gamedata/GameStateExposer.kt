package mcsoc.wildlife.datatrackers.gamedata

import net.minecraft.util.StringRepresentable

enum class GamePhase(val isRunning: Boolean, val isActive: Boolean) : StringRepresentable {
    Inactive(false, false),
    Countdown(true, false),
    Active(true, true);

    override fun getSerializedName(): String = this.name
    
    companion object {
        val CODEC = StringRepresentable.fromEnum(GamePhase::values)
    }
}


interface GameStateExposer {
    val phase: GamePhase
    val pointMultiplier: Int
    
    val activeCard: String?
    
    fun startCountdown(cardId: String)
    fun startGame()
    fun endGame()
    
    fun resetPointMultiplier()
}

internal interface GameDataHolder : GameStateExposer {
    override var phase: GamePhase
    override var pointMultiplier: Int
    override var activeCard: String?
    
    override fun startCountdown(cardId: String) {
        activeCard = cardId
        phase = GamePhase.Countdown
    }
    
    override fun startGame() {
        phase = GamePhase.Active
    }
    
    override fun endGame() {
        phase = GamePhase.Inactive
    }

    override fun resetPointMultiplier() {
        pointMultiplier = 1
    }
}