package mcsoc.wildlife.datatrackers.gamedata

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable

sealed class Wildcard(protected val namespace: String) : StringRepresentable {
    companion object {
        val cardList = Wildcard::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
            .toTypedArray()
                
        val CODEC: Codec<Wildcard> = StringRepresentable.fromValues{cardList}
        
        fun fromNamespace(name: String): Wildcard? {
            for (card in cardList) {
                if (card.getSerializedName() == name) return card
            }
            return null
        }
    }
    
    data object RulesForFools : DatapackWildcard("rulesforfools")
    data object FlowerPower : DatapackWildcard("flowerpower")
    
    data object ControlSwap : Wildcard("controlswap") {
        override fun init() {
            // some initControlSwap()
        }
        override fun start() {
            // some startControlSwap()
        }
        override fun end() {
            // some endControlSwap()
        }
        override fun terminate() {
            // some terminateControlSwap()
        }
    }
    
    
    sealed class DatapackWildcard(namespace: String) : Wildcard(namespace) {
        override fun init() {
            // run function namespace/initialise
        }
        override fun start() {
            // run function namespace/start
        }
        override fun end() {
            // run function namespace/end
        }
        override fun terminate() {
            // run function namespace/terminate
        }
    }
    
    abstract fun init()
    abstract fun start()
    abstract fun end()
    abstract fun terminate()

    override fun getSerializedName(): String = namespace
}


enum class GamePhase(val isRunning: Boolean, val isActive: Boolean) : StringRepresentable {
    Inactive(false, false),
    Countdown(true, false),
    Active(true, true),
    PostGame(true, false);

    override fun getSerializedName(): String = this.name
    
    companion object {
        val CODEC = StringRepresentable.fromEnum(GamePhase::values)
    }
}


interface GameStateExposer {
    val phase: GamePhase
    val pointMultiplier: Int
    
    val activeCard: Wildcard?
    
    fun startCountdown(wildcard: Wildcard)
    fun startGame()
    fun endGame()
    fun terminate()
    
    fun resetPointMultiplier()
}

internal interface GameDataHolder : GameStateExposer {
    override var phase: GamePhase
    override var pointMultiplier: Int
    override var activeCard: Wildcard?
    
    override fun startCountdown(wildcard: Wildcard) {
        activeCard = wildcard
        phase = GamePhase.Countdown
    }
    
    override fun startGame() {
        phase = GamePhase.Active
        activeCard?.start()
    }
    
    override fun endGame() {
        phase = GamePhase.PostGame
        activeCard?.end()
    }

    override fun terminate() {
        activeCard?.terminate()
        activeCard = null
        phase = GamePhase.Inactive
    }

    override fun resetPointMultiplier() {
        pointMultiplier = 1
    }
}