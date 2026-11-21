package mcsoc.wildlife.eventhandlers.commands

import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.UuidArgument


const val ROOT_NODE = "wildlife"
 
internal const val EXAMPLE_ARGUMENT = "node"
private val EXAMPLE_ARG_EXAMPLE get() = Commands.argument(EXAMPLE_ARGUMENT, UuidArgument.uuid())

internal const val DATAPACK_ARGUMENT = "datapack"
private val DATAPACK_ARG get() = Commands.argument(DATAPACK_ARGUMENT, StringArgumentType.word())
    .suggests(DatapackSuggestionProvider())
    
internal const val WILDCARD_ARGUMENT = DATAPACK_ARGUMENT
private val WILDCARD_ARG get() = Commands.argument(WILDCARD_ARGUMENT, StringArgumentType.word())
    .suggests(WildcardSuggestionProvider())

fun registerCommands() {
    CommandRegistrationCallback.EVENT.register{dispatcher, buildContext, selection -> dispatcher.register(
    Commands.literal(ROOT_NODE)
        .then(Commands.literal("example")
            .executes(CommandActions::example)
        )
        .then(Commands.literal("game")
            .then(Commands.literal("start")
                .then(WILDCARD_ARG
                    .executes(CommandActions::startWildcard)
                )
            )
            .then(Commands.literal("end")
                .executes(CommandActions::endWildcard)
            )
        )
    )}
}