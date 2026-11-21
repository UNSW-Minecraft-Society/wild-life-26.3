package mcsoc.wildlife.tests

import mcsoc.wildlife.WildLife
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.coordinates.RotationArgument
import net.minecraft.commands.arguments.coordinates.Vec3Argument


private const val ROOT_NODE = "wildlifetest"

fun registerTestCommands() {
    CommandRegistrationCallback.EVENT.register{dispatcher, buildContext, selection -> dispatcher.register(
    Commands.literal(ROOT_NODE)
        .then(Commands.literal("test")
            .executes {
                WildLife.LOGGER.info("test command")
                1
            }
        )
    )}
}