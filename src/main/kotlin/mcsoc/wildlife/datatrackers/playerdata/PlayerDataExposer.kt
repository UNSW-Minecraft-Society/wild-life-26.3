package mcsoc.wildlife.datatrackers.playerdata

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.playerData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.*


class PlayerData(val playerName: String) {
    // timer data probably not needed but just incase
    // val timer = object : TimeHolder {
    //     override var time: Duration = Duration.ZERO
    //     override fun tick() { time += 1.ticks }
    //     override fun reset() { time = Duration.ZERO }
    // }
    var points = 0
    
    companion object {
        val CODEC: Codec<PlayerData> = RecordCodecBuilder.create{it.group(
            Codec.STRING.fieldOf("playerName").forGetter(PlayerData::playerName),
            Codec.INT.fieldOf("points").forGetter(PlayerData::points),
        ).apply(it, ::PlayerData)}
    }
    
    private constructor(playerName: String, points: Int) : this(playerName) {
        this.points = points
    }
        
    fun resetData() {
        // timer.reset()
        points = 0
    }
    
    override fun toString(): String = 
        "Player Info:\n" +
        // "  time: ${timer.time}\n"
        "  points: ${points}\n"
}


interface PlayerDataExposer {
    val player_data: Map<UUID, PlayerData>
    fun getOrInitPlayerData(player: UUID, playerName: String = "Err"): PlayerData
    fun getOrInitPlayerData(player: Player) = getOrInitPlayerData(player.uuid, player.displayName.string)
    
    val activePlayers: MutableCollection<UUID>
    val activePlayerData: Iterable<PlayerData>
        get() = player_data.filter{it.key in activePlayers}.values
}

fun ServerLevel.getActivePlayers() = this.playerData.activePlayers.shuffled().mapNotNull(this.server.playerList::getPlayer)


internal interface PlayerDataHolder : PlayerDataExposer {
    override val player_data: MutableMap<UUID, PlayerData>
    override fun getOrInitPlayerData(player: UUID, playerName: String): PlayerData = player_data.getOrPut(player){PlayerData(playerName)}
}