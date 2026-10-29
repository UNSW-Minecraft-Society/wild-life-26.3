package mcsoc.wildlife.datatrackers.playerdata

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting

sealed class LifeState(val id: Byte, val next: LifeState?, val prev: LifeState?, colour: ChatFormatting) {
    companion object {
        fun fromId(id: Byte): LifeState {
            return when (id.toInt()) {
                0 -> GREEN
                1 -> YELLOW
                2 -> RED
                3 -> ELIMINATED
                else -> GREEN
            }
        }
        
        val CODEC: Codec<LifeState> = RecordCodecBuilder.create{it.group(
            Codec.BYTE.fieldOf("life_state").forGetter(LifeState::id)
        ).apply(it, ::fromId)}
    }
    
    object GREEN : LifeState(0, LifeState.YELLOW, null, ChatFormatting.GREEN)
    object YELLOW : LifeState(1, LifeState.RED, LifeState.GREEN, ChatFormatting.YELLOW)
    object RED : LifeState(2, LifeState.ELIMINATED, LifeState.YELLOW, ChatFormatting.RED)
    object ELIMINATED : LifeState(3, null, LifeState.RED, ChatFormatting.GRAY)
}