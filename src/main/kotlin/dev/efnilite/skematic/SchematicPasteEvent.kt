package dev.efnilite.skematic

import dev.efnilite.vilib.event.EventWrapper
import org.bukkit.Location

data class SchematicPasteEvent(
    val location: Location,
    val schematic: String,
    val isIgnoringAir: Boolean
) : EventWrapper()