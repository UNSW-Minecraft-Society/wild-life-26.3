package mcsoc.wildlife.datatrackers.gameevents

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData
import net.minecraft.server.level.ServerLevel
import java.util.PriorityQueue


class GameEventsTracker() : LevelTiedData(), GameEventsHolder {
    override fun getType() = LevelDataType.EventsQueue
    companion object {
        val CODEC: MapCodec<GameEventsTracker> = RecordCodecBuilder.mapCodec{it.group(
            GameEvent.CODEC.listOf().fieldOf("queuedEvents").forGetter(GameEventsTracker::listQueuedEvents)
        ).apply(it, ::GameEventsTracker)}
    }
    
    private constructor(eventList: List<GameEvent>) : this() {
        queueEvents(eventList)
    }
    
    override lateinit var level: ServerLevel
    override val eventQueue = PriorityQueue<GameEvent>()
    
    private fun listQueuedEvents(): List<GameEvent> = eventQueue.toList()
}