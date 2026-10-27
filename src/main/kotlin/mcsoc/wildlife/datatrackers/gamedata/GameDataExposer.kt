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
    
    fun startCountdown()
    fun startGame()
    fun end()
}

internal interface GameDataHolder : GameDataExposer {
    override var phase: GamePhase
    
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
}