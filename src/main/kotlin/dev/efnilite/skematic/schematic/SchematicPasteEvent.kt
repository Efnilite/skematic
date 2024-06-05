package dev.efnilite.skematic.schematic

import dev.efnilite.vilib.event.EventWrapper
import org.bukkit.Location

data class SchematicPasteEvent(
    val location: Location,
    val schematicData: SchematicData,
    val isIgnoringAir: Boolean
) : EventWrapper()