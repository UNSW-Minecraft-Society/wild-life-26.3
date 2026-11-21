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
        
        ctx.source.level.startCountdown(datapack)
        ctx.source.sendSystemMessage(Component.literal("Started game"))
        
        return 1
    }
    
    fun endWildcard(ctx: CommandContext<CommandSourceStack>): Int {
        val datapack = StringArgumentType.getString(ctx, DATAPACK_ARGUMENT)

        // val out1 = runFunction(datapack, "interface/end", ctx.source)
        // if (!out1) {
        //     ctx.source.sendFailure(Component.literal("Function end doesn't exist"))
        //     return 0
        // }
        
        // val out2 = runFunction(datapack, "interface/terminate", ctx.source)
        // if (!out2) {
        //     ctx.source.sendFailure(Component.literal("Function terminate doesn't exist"))
        //     return 0
        // }
        ctx.source.level.endCurrentRound()
        ctx.source.sendSystemMessage(Component.literal("Ended game"))
        
        return 1
    }
}