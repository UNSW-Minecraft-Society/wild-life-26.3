package mcsoc.wildlife.utils.clientdisplay.scoreboard

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.numbers.NumberFormat
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.ScoreAccess
import net.minecraft.world.scores.ScoreHolder
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import java.util.UUID


interface PlayerTiedScoreExposer {
    fun tryAddObjective(objectiveName: String, displayName: Component,
                        numberFormat: NumberFormat? = null, renderType: ObjectiveCriteria.RenderType = ObjectiveCriteria.RenderType.INTEGER,
                        criteria: ObjectiveCriteria = ObjectiveCriteria.DUMMY, displayAutoUpdate: Boolean = true
    )
    fun removeObjective(objectiveName: String)

    fun addPlayerToTracking(playerId: UUID, objectiveName: String, slots: Iterable<DisplaySlot>)
    fun removePlayerFromTracking(playerId: UUID, objectiveName: String)
    
    fun handlePlayerJoin(player: ServerPlayer)
    
    fun setScore(objectiveName: String, dummyName: String, score: Int, numberFormat: NumberFormat?)
    fun setScore(objectiveName: String, player: ServerPlayer, score: Int, numberFormat: NumberFormat?)
}

interface PlayerScoreboardHandler : PlayerTiedScoreExposer {
    val scoreboard: PlayerTiedScoreboard
    
    override fun tryAddObjective(objectiveName: String, displayName: Component,
            numberFormat: NumberFormat?, renderType: ObjectiveCriteria.RenderType,
            criteria: ObjectiveCriteria, displayAutoUpdate: Boolean
    ) {
        if (scoreboard.getObjective(objectiveName) == null) {
            scoreboard.addObjective(objectiveName, criteria, displayName, renderType, displayAutoUpdate, numberFormat)
        }
        
    }
    
    override fun removeObjective(objectiveName: String) {
        scoreboard.getObjective(objectiveName)?.let(scoreboard::removeObjective)
    }
    
    
    override fun addPlayerToTracking(playerId: UUID, objectiveName: String, slots: Iterable<DisplaySlot>) {
        scoreboard.getObjective(objectiveName)?.let{
            scoreboard.addTrackedPlayer(playerId, it, slots)
        }
    }

    override fun removePlayerFromTracking(playerId: UUID, objectiveName: String) {
        scoreboard.getObjective(objectiveName)?.let{
            scoreboard.removeTrackedPlayer(playerId, it)
        }
    }
    
    override fun handlePlayerJoin(player: ServerPlayer) {
        scoreboard.sendStartTrackingPacketsTo(player)
    }
    
    
    override fun setScore(objectiveName: String, dummyName: String, score: Int, numberFormat: NumberFormat?) {
        getDummyScoreAccess(objectiveName, dummyName)?.also{
            it.set(score)
            it.numberFormatOverride(numberFormat)
        }
    }
    override fun setScore(objectiveName: String, player: ServerPlayer, score: Int, numberFormat: NumberFormat?) {
        getPlayerScoreAccess(objectiveName, player)?.also{
            it.set(score)
            it.numberFormatOverride(numberFormat)
        }
    }
    
    private fun getDummyScoreAccess(objectiveName: String, dummy: String): ScoreAccess? {
        return scoreboard.getObjective(objectiveName)?.let{
            scoreboard.getOrCreatePlayerScore(ScoreHolder.forNameOnly(dummy), it)
        }
    }
    private fun getPlayerScoreAccess(objectiveName: String, player: ServerPlayer): ScoreAccess? {
        return scoreboard.getObjective(objectiveName)?.let{
            scoreboard.getOrCreatePlayerScore(ScoreHolder.fromGameProfile(player.gameProfile), it)
        }
    }
}