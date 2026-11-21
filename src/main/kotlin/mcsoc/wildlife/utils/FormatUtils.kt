package mcsoc.wildlife.utils

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt
import kotlin.time.Duration

val BlockPos.format: String get() = "(${this.x}, ${this.y}, ${this.z})"
val Vec3.format: String get() = "(${this.x}, ${this.y}, ${this.z})"

fun getProgressBar(fraction: Double, size: Int): String {
    val numBar = (fraction * size).roundToInt()
    val numSpace = size - numBar
    return "[" + "||".repeat(numBar) + " ".repeat(numSpace) + "]"
}

val Duration.formatMMSS: String get() = "${this.inWholeMinutes}:${"%02d".format(this.inWholeSeconds - this.inWholeMinutes * 60)}"
