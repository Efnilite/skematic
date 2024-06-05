package dev.efnilite.skematic

import ch.njol.skript.Skript
import dev.efnilite.vilib.ViPlugin
import dev.efnilite.vilib.bstats.bukkit.Metrics
import dev.efnilite.vilib.util.UpdateChecker
import java.io.File

class Skematic : ViPlugin() {

    override fun enable() {
        instance = this
        stopping = false

        instance.dataFolder.mkdirs()

        val addon = Skript.registerAddon(this)

        addon.loadClasses("dev.efnilite.skematic", "lang")

        UpdateChecker.check(this, 61563)

        Metrics(this, 22150)
    }

    override fun disable() {
        stopping = true
    }

    companion object {
        var stopping = false
            private set
        lateinit var instance: Skematic
            private set

        fun String.toSchematicName() = if (endsWith(".json")) this else "$this.json"

        fun String.toSchematic() = File(instance.dataFolder, if (endsWith(".json")) this else "$this.json")
    }
}