package dev.efnilite.skematic.lang

import ch.njol.skript.Skript
import ch.njol.skript.doc.Description
import ch.njol.skript.doc.Examples
import ch.njol.skript.doc.Name
import ch.njol.skript.doc.Since
import ch.njol.skript.lang.Literal
import ch.njol.skript.lang.SkriptEvent
import ch.njol.skript.lang.SkriptParser
import dev.efnilite.skematic.schematic.SchematicPasteEvent
import dev.efnilite.skematic.Skematic.Companion.toSchematicName
import org.bukkit.event.Event

@Name("Paste schematic")
@Description("Fires when a schematic has finished pasting, possibly ignoring air.")
@Examples("on schematic paste of \"lobby\":",
    "\tbroadcast \"Schematic pasted!\""
)
@Since("1.0.0")
class EvtPaste : SkriptEvent() {

    private var ignoringAir = false
    private var schematic: Literal<String>? = null

    override fun check(event: Event): Boolean {
        if (event !is SchematicPasteEvent) return false

        if (ignoringAir) {
            return event.isIgnoringAir
        }
        if (schematic != null) {
            return event.schematicData.name == schematic?.getSingle(event)?.toSchematicName()
        }

        return true
    }

    override fun init(
        args: Array<out Literal<*>?>,
        matchedPattern: Int,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        ignoringAir = parseResult.mark == 1

        if (args[0] != null) {
            schematic = args[0] as Literal<String>
        }

        return true
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "schematic paste" +
                if (schematic != null) " of ${schematic?.toString(event, debug)}" else "" +
                if (ignoringAir) " ignoring air" else ""
    }

    companion object {
        init {
            Skript.registerEvent(
                "Paste schematic", EvtPaste::class.java, SchematicPasteEvent::class.java,
                "schematic paste [of %-string%] [(1¦(ignoring|skipping) air)]"
            )
        }
    }
}