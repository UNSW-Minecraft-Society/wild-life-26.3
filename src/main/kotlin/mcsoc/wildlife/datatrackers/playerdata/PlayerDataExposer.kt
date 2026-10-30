package mcsoc.wildlife.datatrackers.playerdata

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import mcsoc.wildlife.datatrackers.playerData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import java.util.*


class PlayerData(val playerName: String) {
    companion object {
        val CODEC: Codec<PlayerData> = RecordCodecBuilder.create{it.group(
            Codec.STRING.fieldOf("player_name").forGetter(PlayerData::playerName),
            Codec.INT.fieldOf("points").forGetter(PlayerData::points),
            Codec.INT.fieldOf("multiplier").forGetter(PlayerData::multiplier),
            LifeState.CODEC.fieldOf("lifestate").forGetter(PlayerData::lifestate),
        ).apply(it, ::PlayerData)}
    }
    
    var points = 0
    var multiplier = 1
    var lifestate: LifeState = LifeState.GREEN
    // timer data probably not needed but just incase
    // val timer = object : TimeHolder {
    //     override var time: Duration = Duration.ZERO
    //     override fun tick() { time += 1.ticks }
    //     override fun reset() { time = Duration.ZERO }
    // }
    
    private constructor(playerName: String, points: Int, multiplier: Int, lifestate: LifeState) : this(playerName) {
        this.points = points
        this.multiplier = multiplier
        this.lifestate = lifestate
    }
    
    fun onDeath() {
        lifestate = lifestate.next ?: lifestate
    }
    
    fun restoreLife() {
        lifestate = lifestate.prev ?: lifestate
    }
        
    fun resetData() {
        // timer.reset()
        points = 0
        multiplier = 1
        lifestate = LifeState.GREEN
    }
    
    override fun toString(): String = 
        "Player Info:\n" +
        // "  time: ${timer.time}\n"
        "  points: ${points}\n" +
        "  multiplier: ${multiplier}\n" +
        "  lifestate: ${lifestate}\n"
}


interface PlayerDataExposer {
    val player_data: Map<UUID, PlayerData>
    
    fun resetData()
    fun getPlayerData(player: UUID, playerName: String = "Err"): PlayerData
    fun getPlayerData(player: Player) = getPlayerData(player.uuid, player.displayName.string)
    
    val activePlayers: MutableCollection<UUID>
    val activePlayerData: Iterable<PlayerData>
        get() = player_data.filter{it.key in activePlayers}.values
}

fun ServerLevel.getActivePlayers() = this.playerData.activePlayers.shuffled().mapNotNull(this.server.playerList::getPlayer)

internal interface PlayerDataHolder : PlayerDataExposer {
    override val player_data: MutableMap<UUID, PlayerData>
    override fun getPlayerData(player: UUID, playerName: String): PlayerData = player_data.getOrPut(player){PlayerData(playerName)}

    override fun resetData() {
        activePlayerData.forEach(PlayerData::resetData)
        activePlayers.clear()
    }
}