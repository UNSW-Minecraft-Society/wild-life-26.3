package mcsoc.wildlife.eventhandlers.commands

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.UuidArgument


const val ROOT_NODE = "wildlife"
 
internal const val EXAMPLE_ARGUMENT = "node"
private val EXAMPLE_ARG_EXAMPLE get() = Commands.argument(EXAMPLE_ARGUMENT, UuidArgument.uuid())


fun registerCommands() {
    CommandRegistrationCallback.EVENT.register{dispatcher, buildContext, selection -> dispatcher.register(
    Commands.literal(ROOT_NODE)
        .then(Commands.literal("example")
            .executes(CommandActions::example)
        )
    )}
}