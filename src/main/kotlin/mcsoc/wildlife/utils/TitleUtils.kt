package mcsoc.wildlife.utils

import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

fun ServerPlayer.sendTitle(text: Component) {
    this.connection.send(ClientboundClearTitlesPacket(true))
    this.connection.send(ClientboundSetTitlesAnimationPacket(0, 100, 0))
    this.connection.send(ClientboundSetTitleTextPacket(text))
}

fun ServerPlayer.sendTitle(text: Component, subtitle: Component) {
    this.sendTitle(text)
    this.connection.send(ClientboundSetSubtitleTextPacket(subtitle))
}

fun ServerPlayer.sendSound(sound: SoundEvent) {
    this.connection.send(
        ClientboundSoundPacket(
            Holder.direct(sound),
            SoundSource.MASTER, this.x, this.y, this.z,
            1.0F, 1.0F, this.level().getRandom().nextLong()
        )
    )
}

fun ServerPlayer.sendActionbar(text: Component) {
    this.connection.send(ClientboundSetActionBarTextPacket(text))
}