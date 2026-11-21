package mcsoc.wildlife.datatrackers.timer

import kotlin.time.Duration


// some value which holds some instant of time
interface TimeHolder : Comparable<TimeHolder> {
    val time: Duration    
    override fun compareTo(other: TimeHolder): Int = time.compareTo(other.time)
    
    fun tick()
    fun reset()
}

// same as above, but time field is publically settable
interface MutableTimeHolder : TimeHolder {
    override var time: Duration    
}


// a tick accumulator, monotonically increasing except when reset()
interface TickExposer : TimeHolder {
    val timerTick: UInt
    val timerSecond: UInt
}

interface MutableTickExposer : TickExposer, MutableTimeHolder


internal interface TickHolder : TickExposer {
    override var time: Duration
    override var timerTick: UInt
    override var timerSecond: UInt
}