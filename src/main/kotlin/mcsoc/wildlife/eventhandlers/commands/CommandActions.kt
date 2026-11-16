package mcsoc.wildlife.eventhandlers.commands

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import mcsoc.wildlife.WildLife


object CommandActions {
    fun test(ctx: CommandContext<CommandSourceStack>): Int {
        return 1
    }
}