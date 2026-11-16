package mcsoc.wildlife.eventhandlers

import mcsoc.wildlife.eventhandlers.commands.registerCommands


fun registerEvents() {
    registerCommands()
    registerTickEvents()
}
