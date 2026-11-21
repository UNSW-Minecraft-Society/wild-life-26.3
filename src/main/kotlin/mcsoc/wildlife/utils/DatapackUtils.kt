package mcsoc.wildlife.utils

import net.minecraft.commands.CommandSourceStack
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.permissions.PermissionSet
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3


fun runFunction(namespace: String, path: String, source: CommandSourceStack): Boolean {
    val id = Identifier.tryBuild(namespace, path)
    val function = id?.let {
        source.server.functions.get(it).orElse(null)
    }
    if (function == null) {
        return false
    }
    
    source.server.functions.execute(function, source)
    return true
}
fun runFunction(namespace: String, path: String, server: MinecraftServer, pos: Vec3? = null, rot: Vec2? = null, level: ServerLevel? = null): Boolean =
    runFunction(namespace, path, server.createCommandSourceStack()
        .also{s -> pos?.let{s.withPosition(pos)} ?: s}
        .also{s -> rot?.let{s.withRotation(rot)} ?: s}
        .also{s -> level?.let{s.withLevel(level)} ?: s}
    )