package mcsoc.wildlife.eventhandlers.commands

import com.mojang.brigadier.context.CommandContext
import mcsoc.wildlife.WildLife
import net.minecraft.commands.CommandSourceStack


object CommandActions {
    fun example(ctx: CommandContext<CommandSourceStack>): Int {
        WildLife.LOGGER.info("example command")
        return 1
    }
}