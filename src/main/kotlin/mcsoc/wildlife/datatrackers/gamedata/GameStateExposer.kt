package mcsoc.wildlife.datatrackers.gamedata

import com.mojang.serialization.Codec
import mcsoc.wildlife.utils.runFunction
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.StringRepresentable


sealed class Wildcard(protected val namespace: String) : StringRepresentable {
    companion object {
        val cardList = (Wildcard::class.sealedSubclasses + DatapackWildcard::class.sealedSubclasses)
            .mapNotNull { it.objectInstance }
            .toTypedArray()
            
        val CODEC: Codec<Wildcard> = StringRepresentable.fromValues{cardList}
        
        fun fromNamespace(name: String): Wildcard? {
            for (card in cardList) {
                if (card.serializedName == name) return card
            }
            return null
        }
    }
    
    data object Example : DatapackWildcard("example")
    data object RulesForFools : DatapackWildcard("rulesforfools")
    data object FlowerPower : DatapackWildcard("flowerpower")
    
    data object ControlSwap : Wildcard("controlswap") {
        override fun init(level: ServerLevel) {
            // some initControlSwap()
        }
        override fun start(level: ServerLevel) {
            // some startControlSwap()
        }
        override fun end(level: ServerLevel) {
            // some endControlSwap()
        }
        override fun terminate(level: ServerLevel) {
            // some terminateControlSwap()
        }
    }
    
    
    sealed class DatapackWildcard(namespace: String) : Wildcard(namespace) {
        override fun init(level: ServerLevel) {
            // run function namespace/initialise
            runFunction(namespace, "interface/initialise", level.server, level = level)
        }
        override fun start(level: ServerLevel) {
            // run function namespace/start
            runFunction(namespace, "interface/start", level.server, level = level)
        }
        override fun end(level: ServerLevel) {
            // run function namespace/end
            runFunction(namespace, "interface/end", level.server, level = level)
        }
        override fun terminate(level: ServerLevel) {
            // run function namespace/terminate
            runFunction(namespace, "interface/terminate", level.server, level = level)
        }
    }
    
    abstract fun init(level: ServerLevel)
    abstract fun start(level: ServerLevel)
    abstract fun end(level: ServerLevel)
    abstract fun terminate(level: ServerLevel)

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
    val tiedLevel: ServerLevel
    override var phase: GamePhase
    override var pointMultiplier: Int
    override var activeCard: Wildcard?
    
    override fun startCountdown(wildcard: Wildcard) {
        activeCard = wildcard
        phase = GamePhase.Countdown
        activeCard?.init(tiedLevel)
    }
    
    override fun startGame() {
        phase = GamePhase.Active
        activeCard?.start(tiedLevel)
    }
    
    override fun endGame() {
        phase = GamePhase.PostGame
        activeCard?.end(tiedLevel)
    }

    override fun terminate() {
        activeCard?.terminate(tiedLevel)
        activeCard = null
        phase = GamePhase.Inactive
    }

    override fun resetPointMultiplier() {
        pointMultiplier = 1
    }
}