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
import org.bukkit.Location
import org.bukkit.event.Event

@Name("Save")
@Description("Asynchronously saves the specified area to a schematic with the specified name.")
@Examples("save between {_pos1} and {_pos2} to schematic \"lobby\"")
@Since("1.0.0")
class EffSave : Effect() {

    private lateinit var locations: Expression<Location>
    private lateinit var schematic: Expression<String>

    override fun init(
        expressions: Array<out Expression<*>?>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        locations = expressions[0] as Expression<Location>
        schematic = expressions[1] as Expression<String>

        return true
    }

    override fun execute(event: Event) {
        val locations = locations.getArray(event)
        val name = schematic.getSingle(event)

        if (locations == null || name == null) return
        if (locations.size != 2) {
            Skript.error("Saving schematics requires exactly two locations.")
        }

        Schematic.createAsync(locations[0], locations[1], Skematic.instance).thenAccept {
            it.saveAsync(name.toSchematic(), Skematic.instance)

            SchematicLoader.addSchematic(name, it)
        }
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "save area between ${locations.toString(event, debug)} (as|to) schematic ${schematic.toString(event, debug)}"
    }

    companion object {
        init {
            Skript.registerEffect(EffSave::class.java, "save [area] [between] %locations% to schematic %string%")
        }
    }
}