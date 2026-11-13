@file:JvmName("LevelData")
package mcsoc.wildlife.datatrackers

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType
import mcsoc.wildlife.WildLife
import mcsoc.wildlife.datatrackers.displays.BossBarDisplay
import mcsoc.wildlife.datatrackers.displays.ScoreboardDisplay
import mcsoc.wildlife.datatrackers.gamedata.GameStateExposer
import mcsoc.wildlife.datatrackers.gamedata.GameStateTracker
import mcsoc.wildlife.datatrackers.gameevents.GameEventsExposer
import mcsoc.wildlife.datatrackers.gameevents.GameEventsTracker
import mcsoc.wildlife.datatrackers.playerdata.PlayerDataExposer
import mcsoc.wildlife.datatrackers.playerdata.PlayerDataTracker
import mcsoc.wildlife.datatrackers.timer.GameTimer
import mcsoc.wildlife.datatrackers.timer.TickExposer
import mcsoc.wildlife.utils.clientdisplay.PlayerBossBarHandler
import mcsoc.wildlife.utils.clientdisplay.scoreboard.PlayerScoreboardHandler


// each tracker should extend this class
abstract class LevelTiedData {
    companion object {
        internal val CODEC: Codec<LevelTiedData> = LevelDataType.CODEC.dispatch(
            { inst -> inst.getType() },
            { type -> type.codec }
        )
    }
    internal var isDirty: Boolean = false
    protected fun setDirty() {
        this.isDirty = true
    }
    
    abstract fun getType(): LevelDataType<*>
    
    protected lateinit var level: ServerLevel
    internal open fun setTiedLevel(level: ServerLevel) {
        this.level = level
        // override this w/ any other components requiring level
    }
}

private class LevelTiedDataTracker() : SavedData() {
    private val tracked_data: MutableMap<LevelDataType<*>, LevelTiedData> = mutableMapOf()
    constructor(map: Map<LevelDataType<*>, LevelTiedData>) : this() {
        tracked_data.putAll(map)
    }
    
    companion object {
        private val CODEC: Codec<LevelTiedDataTracker> = RecordCodecBuilder.create{it.group(
            Codec.unboundedMap(LevelDataType.CODEC, LevelTiedData.CODEC)
                .fieldOf("level_data")
                .forGetter(LevelTiedDataTracker::tracked_data)
        ).apply(it, ::LevelTiedDataTracker)}
        
        private val TYPE = SavedDataType<LevelTiedDataTracker>(
            WildLife.id("saved_level_data"),
            ::LevelTiedDataTracker, CODEC, DataFixTypes.LEVEL
        )
        fun getLevelData(level: ServerLevel): LevelTiedDataTracker = level.dataStorage.computeIfAbsent(TYPE)
    }
    override fun isDirty(): Boolean = tracked_data.values.any(LevelTiedData::isDirty)
    override fun setDirty(dirty: Boolean) {
        super.setDirty(dirty)
        for (tracker in tracked_data.values) {
            tracker.isDirty = dirty
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <T : LevelTiedData> getDataOfType(type: LevelDataType<T>, level: ServerLevel): T {
        return (this.tracked_data.getOrPut(type){type.default()} as T)
            .also{it.setTiedLevel(level)}
    }
}

private val ServerLevel.levelTiedData get() = LevelTiedDataTracker.getLevelData(this)


// codec can be null, indicating that the datatype should be level-tied but not saved
sealed class LevelDataType<T : LevelTiedData>(val id: String, val default: () -> T, codec: MapCodec<T>? = null) {
    companion object {
        private val REGISTRY: Map<String, LevelDataType<*>> by lazy {
            LevelDataType::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .associateBy { it.id }
        }
        fun fromId(id: String): LevelDataType<*> = REGISTRY[id] ?: throw IllegalArgumentException("Unknown LevelDataType: \"$id\"")
            
        val CODEC: Codec<LevelDataType<*>> = Codec.STRING.xmap(::fromId, LevelDataType<*>::id)
    }
    val codec: MapCodec<T> = codec ?: MapCodec.unit(default)
    
    // put another enum value for each tracked data type
    object GameTime : LevelDataType<GameTimer>("clock", ::GameTimer, GameTimer.CODEC)
    object EventQueue : LevelDataType<GameEventsTracker>("events", ::GameEventsTracker, GameEventsTracker.CODEC)
    object PlayerData : LevelDataType<PlayerDataTracker>("players", ::PlayerDataTracker, PlayerDataTracker.CODEC)
    object BossBar : LevelDataType<BossBarDisplay>("bossbar", ::BossBarDisplay)
    object Scoreboard : LevelDataType<ScoreboardDisplay>("scoreboard", ::ScoreboardDisplay)
    object GameState : LevelDataType<GameStateTracker>("state", ::GameStateTracker, GameStateTracker.CODEC)
}


// put other level-tied data getters here
val ServerLevel.gameClock: TickExposer get() = this.levelTiedData.getDataOfType(LevelDataType.GameTime, this)
val ServerLevel.eventScheduler: GameEventsExposer get() = this.levelTiedData.getDataOfType(LevelDataType.EventQueue, this)
val ServerLevel.playerData: PlayerDataExposer get() = this.levelTiedData.getDataOfType(LevelDataType.PlayerData, this)
val ServerLevel.bossBarDisplay: PlayerBossBarHandler get() = this.levelTiedData.getDataOfType(LevelDataType.BossBar, this)
val ServerLevel.scoreboardDisplay: PlayerScoreboardHandler get() = this.levelTiedData.getDataOfType(LevelDataType.Scoreboard, this)
val ServerLevel.gameState: GameStateExposer get() = this.levelTiedData.getDataOfType(LevelDataType.GameState, this)