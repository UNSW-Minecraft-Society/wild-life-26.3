package mcsoc.wildlife.datatrackers.timer

import kotlin.time.Duration


// some value which holds some instant of time
interface TimeHolder : Comparable<TimeHolder> {
    val time: Duration
    override fun compareTo(other: TimeHolder): Int = time.compareTo(other.time)
    
    fun tick()
    fun reset()
}

// a tick accumulator, monotonically increasing except when reset()
interface TickExposer : TimeHolder {
    val timerTick: UInt
    val timerSecond: UInt
}


internal interface TickHolder : TickExposer {
    override var timerTick: UInt
    override var timerSecond: UInt
}