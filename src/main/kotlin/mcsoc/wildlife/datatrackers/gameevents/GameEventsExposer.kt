package mcsoc.wildlife.datatrackers.gameevents

import mcsoc.wildlife.datatrackers.gameevents.GameEvent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import java.util.Queue
import java.util.UUID
import kotlin.time.Duration


interface GameEventsExposer {
    fun queueEvent(event: GameEvent)
    fun queueEvents(events: Iterable<GameEvent>)
    fun reset()
    
    fun queueGameStartCounter(countdownTime: Duration)

    fun tick()
}

interface GameEventsHolder : GameEventsExposer {
    val eventQueue: Queue<GameEvent>
    fun getCurrTime(): Duration
    fun triggerGameEvent(event: GameEvent)
    
    override fun queueEvent(event: GameEvent) {
        eventQueue.add(event)
    }
    override fun queueEvents(events: Iterable<GameEvent>) {
        eventQueue.addAll(events)
    }
    
    override fun queueGameStartCounter(countdownTime: Duration) {
        queueEvent(GameEvent.GameStartCounterEvent(getCurrTime(), countdownTime))
    }
    
    private fun dequeueEventsToTrigger(): Iterable<GameEvent> {
        val eventsToTrigger: MutableCollection<GameEvent> = mutableSetOf()
        while (eventQueue.isNotEmpty() && eventQueue.peek().hasExpired(getCurrTime()))
            eventsToTrigger.add(eventQueue.poll() ?: break)
        return eventsToTrigger
    }

    override fun reset() {
        this.eventQueue.clear()
    }
    
    override fun tick() {
        for (event in dequeueEventsToTrigger()) triggerGameEvent(event)
    }
}