package mcsoc.wildlife.datatrackers.displays

import com.mojang.serialization.MapCodec
import mcsoc.wildlife.datatrackers.LevelDataType
import mcsoc.wildlife.datatrackers.LevelTiedData
import mcsoc.wildlife.utils.clientdisplay.scoreboard.PlayerScoreboardHandler
import mcsoc.wildlife.utils.clientdisplay.scoreboard.PlayerTiedScoreboard


class ScoreboardDisplay : LevelTiedData(), PlayerScoreboardHandler {
    override fun getType() = LevelDataType.Scoreboard
    companion object {
        // TODO write/port this
        val CODEC: MapCodec<ScoreboardDisplay> = TODO()
    }
    override var scoreboard = PlayerTiedScoreboard()
        private set
}