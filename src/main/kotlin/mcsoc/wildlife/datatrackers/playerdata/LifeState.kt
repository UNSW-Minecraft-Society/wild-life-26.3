package mcsoc.wildlife.datatrackers.playerdata

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting

sealed class LifeState(val id: Byte, val next: LifeState?, val prev: LifeState?, val colour: ChatFormatting) {
    companion object {
        fun fromId(id: Byte): LifeState {
            return when (id.toInt()) {
                0 -> Green
                2 -> Red
                3 -> Inactive
                else -> Green
            }
        }
        
        val CODEC: Codec<LifeState> = RecordCodecBuilder.create{it.group(
            Codec.BYTE.fieldOf("life_state").forGetter(LifeState::id)
        ).apply(it, ::fromId)}
    }
    
    object Green : LifeState(0, LifeState.Red, null, ChatFormatting.GREEN)
    object Red : LifeState(2, LifeState.Inactive, LifeState.Green, ChatFormatting.RED)
    object Inactive : LifeState(3, null, null, ChatFormatting.GRAY)
}