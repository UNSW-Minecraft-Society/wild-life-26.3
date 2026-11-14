package mcsoc.wildlife.datatrackers.gameevents

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData
import mcsoc.wildlife.datatrackers.gameClock
import net.minecraft.server.level.ServerLevel
import java.util.PriorityQueue


class GameEventsTracker() : LevelTiedData(), GameEventsHolder {
    companion object {
        val CODEC: MapCodec<GameEventsTracker> = RecordCodecBuilder.mapCodec{it.group(
            GameEvent.CODEC.listOf().fieldOf("queuedEvents").forGetter(GameEventsTracker::listQueuedEvents)
        ).apply(it, ::GameEventsTracker)}
    }
    override fun getType() = LevelDataType.EventQueue
        
    override val eventQueue = PriorityQueue<GameEvent>()
    private fun listQueuedEvents(): List<GameEvent> = eventQueue.toList()
    
    private constructor(eventList: List<GameEvent>) : this() {
        queueEvents(eventList)
    }

    override fun getCurrTime() = level.gameClock.time
    override fun triggerGameEvent(event: GameEvent) = event.trigger(level)
}