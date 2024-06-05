package dev.efnilite.skematic.lang

import ch.njol.skript.doc.Description
import ch.njol.skript.doc.Name
import ch.njol.skript.doc.Since
import ch.njol.skript.expressions.base.PropertyExpression
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import dev.efnilite.skematic.schematic.SchematicLoader
import org.bukkit.event.Event

@Name("Schematic data version")
@Description("The integer iteration of the schematic format.")
@Since("1.0.0")
class ExprDataVersion : PropertyExpression<String, Int>() {

    override fun init(
        expressions: Array<out Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        expr = expressions[0] as Expression<String>

        return true
    }

    override fun get(event: Event, source: Array<out String>): Array<Int> {
        val dimensions = mutableListOf<Int>()

        for (schematic in source) {
            dimensions += SchematicLoader.getSchematic(schematic).dataVersion
        }

        return dimensions.toTypedArray()
    }

    override fun getReturnType() = Int::class.java

    override fun toString(event: Event?, debug: Boolean) = "schematic data version ${expr.toString(event, debug)}"

    companion object {
        init {
            register(ExprDataVersion::class.java, Int::class.java, "schematic data version", "strings")
        }
    }
}