package mcsoc.wildlife.eventhandlers.commands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.commands.CommandSourceStack
import java.util.concurrent.CompletableFuture

internal class ExampleSuggestionProvider: SuggestionProvider<CommandSourceStack> {
	override fun getSuggestions(ctx: CommandContext<CommandSourceStack>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
		builder.suggest(ctx.source.textName)
		return builder.buildFuture()
	}
}

internal class DatapackSuggestionProvider: SuggestionProvider<CommandSourceStack> {
	override fun getSuggestions(ctx: CommandContext<CommandSourceStack>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
		ctx.source.server.functions.functionNames
			.filter {it.path == "interface/initialise" }
			.map {it.namespace}.distinct()
			.forEach { builder.suggest(it) }
		return builder.buildFuture()
	}
}
