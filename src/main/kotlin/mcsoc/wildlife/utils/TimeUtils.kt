package mcsoc.wildlife.utils

import com.mojang.serialization.Codec
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


inline val Int.ticks: Duration 
    get() = this.milliseconds * 50

inline val Long.ticks: Duration
    get() = this.milliseconds * 50

inline val Duration.inWholeTicks: Long
    get() = (this * 20).inWholeSeconds
    
val Duration.Companion.CODEC: Codec<Duration> 
    get() = Codec.STRING.xmap(Duration::parseIsoString, Duration::toIsoString)