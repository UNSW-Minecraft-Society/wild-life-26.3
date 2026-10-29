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


interface GameDataExposer {
    val phase: GamePhase
    val pointMultiplier: Int
    
    fun startCountdown()
    fun startGame()
    fun end()
    
    fun setPointMultiplier(multiplier: Int)
    fun resetPointMultiplier()
}

internal interface GameDataHolder : GameDataExposer {
    override var phase: GamePhase
    override var pointMultiplier: Int
    
    private fun setGamePhase(phase: GamePhase) {
        this.phase = phase
    }

    override fun startCountdown() {
        setGamePhase(GamePhase.Countdown)
    }
    
    override fun startGame() {
        setGamePhase(GamePhase.Active)
    }
    
    override fun end() {
        setGamePhase(GamePhase.Inactive)
    }

    override fun setPointMultiplier(multiplier: Int) {
        pointMultiplier = multiplier
    }

    override fun resetPointMultiplier() {
        pointMultiplier = 1
    }
}