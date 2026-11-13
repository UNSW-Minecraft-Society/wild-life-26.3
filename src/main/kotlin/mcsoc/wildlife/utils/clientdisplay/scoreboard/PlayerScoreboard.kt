package mcsoc.wildlife.utils.clientdisplay.scoreboard

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.UUIDUtil
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetScorePacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.Objective
import net.minecraft.world.scores.PlayerScoreEntry
import net.minecraft.world.scores.PlayerTeam
import net.minecraft.world.scores.Score
import net.minecraft.world.scores.ScoreHolder
import net.minecraft.world.scores.Scoreboard
import java.util.EnumMap
import java.util.Optional
import java.util.UUID

open class PlayerTiedScoreboard() : Scoreboard() {
    companion object {
        var CODEC: Codec<PlayerTiedScoreboard> = RecordCodecBuilder.create{it.group(
            Objective.Packed.CODEC.listOf().fieldOf("objectives").forGetter(PlayerTiedScoreboard::packObjectives),
            Scoreboard.PackedScore.CODEC.listOf().fieldOf("scores").forGetter(PlayerTiedScoreboard::packPlayerScores),
            Codec.unboundedMap(
                UUIDUtil.STRING_CODEC,
                Codec.unboundedMap(
                    DisplaySlot.CODEC,
                    Codec.STRING
                )
            ).fieldOf("trackedObjectivesMap").forGetter(PlayerTiedScoreboard::packTracking),
        ).apply(it, ::PlayerTiedScoreboard)}
    }
    private constructor(
        objectives: List<Objective.Packed>,
        scores: List<Scoreboard.PackedScore>,
        tracked: Map<UUID, Map<DisplaySlot, String>>
    ) : this() {
        for (objective in objectives) loadObjective(objective)
        for (score in scores) loadPlayerScore(score)
        
        for ((playerId, objectives) in tracked) {
            for ((slot, objective) in objectives) {
                val objective = getObjective(objective) ?: continue
                trackedObjectivesMap.getOrPut(playerId){ EnumMap(DisplaySlot::class.java) }[slot] = objective
                trackingPlayersMap.getOrPut(objective, ::mutableSetOf).add(playerId)
            }
        }
    }
    
    lateinit var level: ServerLevel
    // mapping between player uuid and the objective set in each display slot
    private val trackedObjectivesMap: MutableMap<UUID, EnumMap<DisplaySlot, Objective>> = mutableMapOf()
    // mapping between objective and the players currently tracking it
    private val trackingPlayersMap: MutableMap<Objective, MutableSet<UUID>> = mutableMapOf()
    
    protected fun packTracking(): Map<UUID, Map<DisplaySlot, String>> {
        return trackedObjectivesMap.mapValues{it.value.mapValues{it.value.name}}
    }

    override fun getTrackedPlayers(): Collection<ScoreHolder> {
        return trackedObjectivesMap.keys
            .mapNotNull(level.server.playerList::getPlayer)
            .map(ServerPlayer::getGameProfile)
            .map(ScoreHolder::fromGameProfile)
            .toMutableList().also{
                it.addAll(super.getTrackedPlayers())
            }
    }
    fun addTrackedPlayer(playerId: UUID, objective: Objective, slots: Iterable<DisplaySlot>) {
        for (slot in slots) trackedObjectivesMap.getOrPut(playerId){EnumMap(DisplaySlot::class.java)}[slot] = objective
        trackingPlayersMap.getOrPut(objective, ::mutableSetOf).add(playerId)
        
        level.server.playerList.getPlayer(playerId)?.let(::sendStartTrackingPacketsTo)
    }
    fun removeTrackedPlayer(playerId: UUID, objective: Objective) {
        trackedObjectivesMap[playerId]?.clear()
        trackingPlayersMap.getOrPut(objective, ::mutableSetOf).remove(playerId)
        
        level.server.playerList.getPlayer(playerId)?.let{
            for (packet in createStopTrackingPacketsFor(objective)) it.connection.send(packet)
        }
    }

    private fun createUpdateScorePacketFor(objective: Objective, entry: PlayerScoreEntry): Packet<ClientGamePacketListener> {
        return ClientboundSetScorePacket(
            entry.owner,
            objective.name,
            entry.value,
            Optional.ofNullable(entry.display),
            Optional.ofNullable(entry.numberFormatOverride)
        )
    }
    private fun createUpdateScorePacketFor(objective: Objective, owner: ScoreHolder, score: Score): Packet<ClientGamePacketListener> {
        return ClientboundSetScorePacket(
            owner.scoreboardName,
            objective.name,
            score.value(),
            Optional.ofNullable(score.display()),
            Optional.ofNullable(score.numberFormat())
        )
    }
    
    /* Constructs packets for updating every score on given objective
     * Arguments
     * - objective: The objective to create score packets for
     */
    private fun createUpdateScorePacketsFor(objective: Objective): Iterable<Packet<ClientGamePacketListener>> {
        val packets: MutableCollection<Packet<ClientGamePacketListener>> = mutableSetOf()
        for (entry in this.listPlayerScores(objective)) {
            packets.add(createUpdateScorePacketFor(objective, entry))
        }
        return packets
    }
    
    /* Constructs all tracking packets for the given objective
     * Arguments
     * - objective: The objective to create tracking packets for
     */
    private fun createStartTrackingPacketsFor(objective: Objective): Iterable<Packet<ClientGamePacketListener>> {
        val packets: MutableCollection<Packet<ClientGamePacketListener>> = mutableListOf()
        packets.add(
            ClientboundSetObjectivePacket(
                objective,
                ClientboundSetObjectivePacket.METHOD_ADD
            )
        )
        packets.addAll(createUpdateScorePacketsFor(objective))
        
        return packets
    }
    fun sendStartTrackingPacketsTo(player: ServerPlayer) {
        val packets: MutableCollection<Packet<ClientGamePacketListener>> = mutableListOf()
        val trackedObjectives: MutableSet<Objective> = mutableSetOf()
        trackedObjectivesMap[player.uuid]?.let{
            for ((slot, objective) in it) {
                if (objective !in trackedObjectives) {
                    packets.addAll(createStartTrackingPacketsFor(objective))
                    trackedObjectives.add(objective)
                }
                packets.add(ClientboundSetDisplayObjectivePacket(slot, objective))
            }
        }
        for (packet in packets) player.connection.send(packet)
    }
    
    
    /* Constructs all untracking packets for the given objective
     * Arguments
     * - objective: The objective to create untracking packets for
     */
    fun createStopTrackingPacketsFor(objective: Objective): Iterable<Packet<ClientGamePacketListener>> {
        val packets: MutableCollection<Packet<ClientGamePacketListener>> = mutableListOf()
        packets.add(ClientboundSetObjectivePacket(
            objective,
            ClientboundSetObjectivePacket.METHOD_REMOVE
        ))
        
        return packets
    }
    

    override fun onObjectiveChanged(objective: Objective) {
    }

    override fun onObjectiveRemoved(objective: Objective) {
        trackingPlayersMap[objective]?.let{
            for (playerId in it.toList()) {
                removeTrackedPlayer(playerId, objective)
            }
        }
    }

    override fun onScoreChanged(owner: ScoreHolder, objective: Objective, score: Score) {
        super.onScoreChanged(owner, objective, score)
        val trackingPlayers = trackingPlayersMap[objective] ?: return
        for (playerId in trackingPlayers) {
            val player = level.server.playerList.getPlayer(playerId) ?: continue
            player.connection.send(createUpdateScorePacketFor(objective, owner, score))
        }
    }
    
    override fun onScoreLockChanged(owner: ScoreHolder, objective: Objective) {
    }

    override fun onPlayerRemoved(player: ScoreHolder) {
    }

    override fun onPlayerScoreRemoved(player: ScoreHolder, objective: Objective) {
    }

    override fun onTeamAdded(team: PlayerTeam) {
    }

    override fun onTeamChanged(team: PlayerTeam) {
    }

    override fun onTeamRemoved(team: PlayerTeam) {
    }
}