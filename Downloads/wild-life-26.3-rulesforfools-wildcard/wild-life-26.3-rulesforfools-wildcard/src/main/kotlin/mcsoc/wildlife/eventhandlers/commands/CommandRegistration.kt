package mcsoc.wildlife.eventhandlers.commands

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.UuidArgument


const val ROOT_NODE = "wildlife"
 
internal const val NODE_ARGUMENT = "node"
private val NODE_ARGUMENT_NODE get() = Commands.argument(NODE_ARGUMENT, UuidArgument.uuid())


fun registerCommands() {
    CommandRegistrationCallback.EVENT.register{dispatcher, buildContext, selection -> dispatcher.register(
    Commands.literal(ROOT_NODE)
        .then(Commands.literal("test")
            .executes(CommandActions::test)
        )
    )}
}