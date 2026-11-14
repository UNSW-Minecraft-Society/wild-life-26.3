package mcsoc.wildlife

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

object WildLife : ModInitializer {
	const val MOD_ID: String = "wild-life"
	val LOGGER = LoggerFactory.getLogger(MOD_ID)!!

	override fun onInitialize() {
		// runs as soon as Minecraft is in a mod-load-ready state.
		LOGGER.info("Hello Fabric world!")
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
