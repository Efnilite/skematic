package dev.efnilite.skematic.schematic

import dev.efnilite.neoschematic.Schematic
import org.bukkit.util.Vector

/**
 * Represents a schematic file's main properties.
 * To reduce memory usage, palette and blocks are not included.
 */
data class SchematicData(
    val name: String,
    val dataVersion: Int,
    val minecraftVersion: String,
    val dimensions: Vector
) {

    constructor(name: String, schematic: Schematic) : this(name, schematic.dataVersion, schematic.minecraftVersion, schematic.dimensions)

}