package dev.efnilite.skematic.lang

import ch.njol.skript.Skript
import ch.njol.skript.doc.Description
import ch.njol.skript.doc.Examples
import ch.njol.skript.doc.Name
import ch.njol.skript.doc.Since
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import dev.efnilite.neoschematic.Schematic
import dev.efnilite.skematic.Skematic
import dev.efnilite.skematic.Skematic.Companion.toSchematic
import dev.efnilite.skematic.schematic.SchematicLoader
import dev.efnilite.skematic.schematic.SchematicPasteEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.Event

@Name("Paste schematic")
@Description("Asynchronously pastes specified schematic at specified location, possibly ignoring air.")
@Examples("paste schematic \"spawn.json\" at player's location ignoring air")
@Since("1.0.0")
class EffPaste : Effect() {

    private var ignoringAir = false
    private lateinit var schematic: Expression<String>
    private lateinit var location: Expression<Location>

    @Suppress("UNCHECKED_CAST")
    override fun init(
        expressions: Array<out Expression<*>?>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        ignoringAir = parseResult.mark == 1
        schematic = expressions[0] as Expression<String>
        location = expressions[1] as Expression<Location>

        return true
    }

    override fun execute(event: Event) {
        val name = schematic.getSingle(event)
        val location = location.getSingle(event)

        if (name == null || location == null) return

        if (!name.toSchematic().exists()) {
            Skript.error("Schematic $name does not exist.")
            return
        }

        Schematic.loadAsync(name.toSchematic(), Skematic.instance).thenApply {
            Bukkit.getScheduler().runTask(Skematic.instance, Runnable {
                it.paste(location, ignoringAir)

                Bukkit.getPluginManager().callEvent(SchematicPasteEvent(location, name, SchematicLoader.getSchematic(name), ignoringAir))
            })
        }
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "paste schematic ${schematic.toString(event, debug)} at ${location.toString(event, debug)}" + if (ignoringAir) " ignoring air" else ""
    }

    companion object {

        init {
            Skript.registerEffect(EffPaste::class.java, "paste schematic %string% (at|to) %location% [(1¦(ignoring|skipping) air)]")
        }

    }
}