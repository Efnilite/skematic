package dev.efnilite.skematic.schematic

import ch.njol.skript.Skript
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
                schematics[name] = SchematicData(name, it)
            }
        }
    }

    /**
     * Adds a schematic to the loader.
     * @param name The name of the schematic.
     * @param schematic The schematic.
     */
    fun addSchematic(name: String, schematic: Schematic) {
        schematics[name] = SchematicData(name, schematic)
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
            Skript.error("Schematic $name does not exist.")
        }

        val schematic = Schematic.load(file)

        if (schematic == null) {
            Skript.error("Failed to load schematic $name.")
        }

        return SchematicData(name, schematic!!)
    }
}