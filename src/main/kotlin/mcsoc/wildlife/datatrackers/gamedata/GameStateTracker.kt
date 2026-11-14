package mcsoc.wildlife.datatrackers.gamedata

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData


class GameStateTracker() : LevelTiedData(), GameDataHolder {
    override fun getType() = LevelDataType.GameState
    companion object {
        val CODEC: MapCodec<GameStateTracker> = RecordCodecBuilder.mapCodec{it.group(
            GamePhase.CODEC.fieldOf("game_phase").forGetter(GameStateTracker::phase),
            Codec.INT.fieldOf("point_multiplier").forGetter(GameStateTracker::pointMultiplier)
        ).apply(it, ::GameStateTracker)}
    }
    private constructor(phase: GamePhase, multiplier: Int) : this() {
        this.phase = phase
        pointMultiplier = multiplier
    }
    
    override var phase: GamePhase = GamePhase.Inactive
    override var pointMultiplier = 1
}