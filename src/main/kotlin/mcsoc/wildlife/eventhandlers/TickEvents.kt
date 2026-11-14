package mcsoc.wildlife.eventhandlers

import mcsoc.wildlife.datatrackers.eventScheduler
import mcsoc.wildlife.datatrackers.gameClock
import mcsoc.wildlife.gameeffects.tickGame
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents


fun registerTickEvents() {
    registerGameClockTick()
    registerGameEventsTick()
    registerPlayerTrackerTick()
}

fun registerGameClockTick() {
    ServerTickEvents.START_LEVEL_TICK.register{ it.gameClock.tick() }
}

fun registerGameEventsTick() {
    ServerTickEvents.START_LEVEL_TICK.register{ it.eventScheduler.tick() }
}

fun registerPlayerTrackerTick() {
    ServerTickEvents.END_LEVEL_TICK.register{level ->
        for (i in 0u until level.gameClock.timerTick) {
            level.tickGame()
        }
    }
}