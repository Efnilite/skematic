package dev.efnilite.skematic.schematic

import dev.efnilite.neoschematic.Schematic
import dev.efnilite.skematic.Skematic
import dev.efnilite.skematic.Skematic.Companion.toSchematic

object SchematicLoader {

    private val schematics = mutableMapOf<String, SchematicData>()

    init {
        val inFolder = Skematic.instance.dataFolder.listFiles { _, name -> name.endsWith(".json") } ?: arrayOf()

        for (file in inFolder) {
            val name = file.nameWithoutExtension

            Schematic.loadAsync(file, Skematic.instance).thenAccept {
                schematics[name] = SchematicData(it)
            }
        }
    }

    /**
     * Adds a schematic to the loader.
     * @param name The name of the schematic.
     * @param schematic The schematic.
     */
    fun addSchematic(name: String, schematic: Schematic) {
        schematics[name] = SchematicData(schematic)
    }

    /**
     * Gets a schematic by its name.
     * @param name The name of the schematic.
     */
    fun getSchematic(name: String): SchematicData {
        if (schematics.containsKey(name)) {
            return schematics[name]!!
        }

        val file = name.toSchematic()

        if (!file.exists()) {
            throw IllegalArgumentException("Schematic $name does not exist.")
        }

        val schematic = Schematic.load(file) ?: throw IllegalArgumentException("Failed to load schematic $name.")

        return SchematicData(schematic)
    }
}