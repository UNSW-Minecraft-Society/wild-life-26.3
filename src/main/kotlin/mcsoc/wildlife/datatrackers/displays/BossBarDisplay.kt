package mcsoc.wildlife.datatrackers.displays

import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData
import mcsoc.wildlife.utils.clientdisplay.PlayerBossBarHandler
import net.minecraft.server.bossevents.CustomBossEvent
import net.minecraft.server.level.ServerPlayer
import java.util.UUID


class BossBarDisplay : LevelTiedData(), PlayerBossBarHandler {
    override fun getType() = LevelDataType.BossBar
    companion object {
        // TODO write/port this
        val CODEC = null
    }
    override val bossEventMap: MutableMap<UUID, CustomBossEvent> = mutableMapOf()
    override fun getServerPlayer(playerId: UUID): ServerPlayer? = level.server.playerList.getPlayer(playerId)
}