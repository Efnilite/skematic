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

@Name("Schematic minecraft version")
@Description("The Minecraft version in which a schematic was saved.")
@Examples("if schematic minecraft version of \"lobby\" isn't \"1.20.6\":",
    "\tsend \"Outdated schematic file.\" to player")
@Since("1.0.0")
class ExprMinecraftVersion : PropertyExpression<String, String>() {

    override fun init(
        expressions: Array<out Expression<*>?>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        expr = expressions[0] as Expression<String>

        return true
    }

    override fun get(event: Event, source: Array<out String>): Array<String> {
        val dimensions = mutableListOf<String>()

        for (schematic in source) {
            dimensions += SchematicLoader.getSchematic(schematic).minecraftVersion
        }

        return dimensions.toTypedArray()
    }

    override fun getReturnType() = String::class.java

    override fun toString(event: Event?, debug: Boolean) = "schematic minecraft version ${expr.toString(event, debug)}"

    companion object {
        init {
            register(ExprMinecraftVersion::class.java, String::class.java, "schematic minecraft version", "strings")
        }
    }
}