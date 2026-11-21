package mcsoc.wildlife.datatrackers.gamedata

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData
import java.util.Optional
import kotlin.jvm.optionals.getOrNull


class GameStateTracker() : LevelTiedData(), GameDataHolder {
    override fun getType() = LevelDataType.GameState
    companion object {
        val CODEC: MapCodec<GameStateTracker> = RecordCodecBuilder.mapCodec{it.group(
            GamePhase.CODEC.fieldOf("game_phase").forGetter(GameStateTracker::phase),
            Codec.INT.fieldOf("point_multiplier").forGetter(GameStateTracker::pointMultiplier),
            Wildcard.CODEC.optionalFieldOf("activeCard").forGetter(GameStateTracker::activeCardOptional)
        ).apply(it, ::GameStateTracker)}
    }
    private constructor(phase: GamePhase, multiplier: Int, activeCard: Optional<Wildcard>) : this() {
        this.phase = phase
        pointMultiplier = multiplier
        this.activeCard = activeCard.getOrNull()
    }
    
    override var phase: GamePhase = GamePhase.Inactive
    override var pointMultiplier = 1
    override var activeCard: Wildcard? = null
        get() = if (phase.isRunning) field else null
    private val activeCardOptional get() = Optional.ofNullable(activeCard)
}