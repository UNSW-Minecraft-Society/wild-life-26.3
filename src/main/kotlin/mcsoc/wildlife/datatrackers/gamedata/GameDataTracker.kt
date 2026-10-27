package mcsoc.wildlife.datatrackers.gamedata

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData


class GameDataTracker() : LevelTiedData(), GameDataHolder {
    override fun getType() = LevelDataType.GameData
    companion object {
        val CODEC: MapCodec<GameDataTracker> = RecordCodecBuilder.mapCodec{it.group(
            GamePhase.CODEC.fieldOf("gamePhase").forGetter(GameDataTracker::phase)
        ).apply(it, ::GameDataTracker)}
    }
    private constructor(phase: GamePhase) : this() {
        this.phase = phase
    }
    
    override var phase: GamePhase = GamePhase.Inactive
}