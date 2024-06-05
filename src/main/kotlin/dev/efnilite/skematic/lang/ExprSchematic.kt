package dev.efnilite.skematic.lang

import ch.njol.skript.doc.Description
import ch.njol.skript.doc.Name
import ch.njol.skript.doc.Since
import ch.njol.skript.expressions.base.EventValueExpression

@Name("Schematic")
@Description("The name of the schematic involved in a schematic paste event.")
@Since("1.0.0")
class ExprSchematic : EventValueExpression<String>(String::class.java) {

    companion object {
        init {
            register(ExprSchematic::class.java, String::class.java, "schematic")
        }
    }
}