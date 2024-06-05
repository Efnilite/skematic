package dev.efnilite.skematic.lang

import ch.njol.skript.Skript
import ch.njol.skript.doc.Description
import ch.njol.skript.doc.Events
import ch.njol.skript.doc.Name
import ch.njol.skript.doc.Since
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ExpressionType
import ch.njol.skript.lang.SkriptParser
import ch.njol.skript.lang.util.SimpleExpression
import ch.njol.util.Kleenean
import dev.efnilite.skematic.schematic.SchematicPasteEvent
import org.bukkit.event.Event

@Name("Schematic")
@Description("The name of the schematic involved in a schematic paste event.")
@Since("1.0.0")
@Events("Paste schematic")
class ExprSchematic : SimpleExpression<String>() {

    override fun init(
        expressions: Array<out Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ) = true

    override fun get(event: Event): Array<String> {
        if (event !is SchematicPasteEvent) return emptyArray()

        return arrayOf(event.name)
    }

    override fun isSingle() = true

    override fun getReturnType() = String::class.java

    override fun toString(event: Event?, debug: Boolean) = "schematic"

    companion object {
        init {
            Skript.registerExpression(ExprSchematic::class.java, String::class.java, ExpressionType.EVENT, "[pasted] [event-]schematic")
        }
    }
}