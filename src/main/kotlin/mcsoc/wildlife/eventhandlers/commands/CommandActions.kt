package mcsoc.wildlife.eventhandlers.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import mcsoc.wildlife.WildLife
import mcsoc.wildlife.gameeffects.endCurrentRound
import mcsoc.wildlife.gameeffects.startCountdown
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component


object CommandActions {
    fun example(ctx: CommandContext<CommandSourceStack>): Int {
        WildLife.LOGGER.info("example command")
        return 1
    }
    
    fun startWildcard(ctx: CommandContext<CommandSourceStack>): Int {
        val datapack = StringArgumentType.getString(ctx, DATAPACK_ARGUMENT)

        // val out1 = runFunction(datapack, "interface/initialise", ctx.source)
        // if (!out1) {
        //     ctx.source.sendFailure(Component.literal("Function initialise doesn't exist"))
        //     return 0
        // }
        
        // val out2 = runFunction(datapack, "interface/start", ctx.source)
        // if (!out2) {
        //     ctx.source.sendFailure(Component.literal("Function start doesn't exist"))
        //     return 0
        // }
        
        return ctx.source.level.startCountdown(datapack).let{
            // TODO add more branches if
            when (it) {
                0 -> ctx.source.sendSuccess({Component.literal("Initiated $datapack.")}, true)
                1 -> ctx.source.sendFailure(Component.literal("No wildcard with that name is defined!"))
                else -> ctx.source.sendFailure(Component.literal("An error!"))
            }
            1 - it
        }
    }
    
    fun endWildcard(ctx: CommandContext<CommandSourceStack>): Int {
        
        // trusting that end and terminate are not malformed ig
        return ctx.source.level.endCurrentRound().let{
            when (it) {
                0 -> ctx.source.sendSuccess({Component.literal("Ended game")}, true)
                1 -> ctx.source.sendFailure(Component.literal("No game is currently running!"))
                else -> {}
            }
            
            1 - it
        }
    }
}