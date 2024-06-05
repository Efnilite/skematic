package dev.efnilite.skematic.lang

import ch.njol.skript.doc.Description
import ch.njol.skript.doc.Examples
import ch.njol.skript.doc.Name
import ch.njol.skript.doc.Since
import ch.njol.skript.expressions.base.PropertyExpression
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import dev.efnilite.skematic.schematic.SchematicLoader
import org.bukkit.event.Event
import org.bukkit.util.Vector

@Name("Schematic dimensions")
@Description("The vector dimensions of a schematic.")
@Examples("set {_dimensions} to schematic dimensions of \"lobby\"")
@Since("1.0.0")
class ExprDimensions : PropertyExpression<String, Vector>() {

    override fun init(
        expressions: Array<out Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        expr = expressions[0] as Expression<String>

        return true
    }

    override fun get(event: Event, source: Array<out String>): Array<Vector> {
        val dimensions = mutableListOf<Vector>()

        for (schematic in source) {
            dimensions += SchematicLoader.getSchematic(schematic).dimensions
        }

        return dimensions.toTypedArray()
    }

    override fun getReturnType() = Vector::class.java

    override fun toString(event: Event?, debug: Boolean) = "schematic dimensions of ${expr.toString(event, debug)}"

    companion object {
        init {
            register(ExprDimensions::class.java, Vector::class.java, "schematic dimensions", "strings")
        }
    }
}