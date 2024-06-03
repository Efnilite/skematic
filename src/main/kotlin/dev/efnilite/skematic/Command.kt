package dev.efnilite.skematic

import dev.efnilite.neoschematic.Schematic
import dev.efnilite.skematic.Skematic.Companion.toSchematic
import dev.efnilite.skematic.Skematic.Companion.toSchematicName
import dev.efnilite.vilib.command.ViCommand
import dev.efnilite.vilib.util.Locations
import dev.efnilite.vilib.util.Strings
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Command : ViCommand() {

    private val pos1s = mutableMapOf<CommandSender, Location>()
    private val pos2s = mutableMapOf<CommandSender, Location>()

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> {
                sender.send("<gradient:#800000:#cc0000><bold>Skematic")
                sender.send("<#ff6666>/skematic paste <name> <gray>- Paste a schematic")
                sender.send("<#ff6666>/skematic save <name> <gray>- Save a schematic")
                sender.send("<#ff6666>/skematic pos1 <gray>- Set position 1")
                sender.send("<#ff6666>/skematic pos2 <gray>- Set position 2")
            }
            1 -> {
                if (sender !is Player || !sender.isOp) return true

                when (args[0].lowercase()) {
                    "paste" -> {
                        sender.send("<gray>Usage: /skematic paste <schematic>")
                    }
                    "save" -> {
                        sender.send("<gray>Usage: /skematic save <name>")
                    }
                    "pos1" -> {
                        pos1s[sender] = sender.location

                        sender.send("<gray>Position 1 set to ${Locations.toString(sender.location, true)}")
                    }
                    "pos2" -> {
                        pos2s[sender] = sender.location

                        sender.send("<gray>Position 2 set to ${Locations.toString(sender.location, true)}")
                    }
                    else -> return false
                }
            }
            2 -> {
                if (sender !is Player || !sender.isOp) return true

                when (args[0].lowercase()) {
                    "paste" -> {
                        val name = args[1].toSchematicName()
                        val schematic = name.toSchematic()

                        if (!schematic.exists()) {
                            sender.send("<#cc0000>$name does not exist.")
                            return true
                        }

                        sender.send("<gray>Loading $name...")
                        Schematic.loadAsync(schematic, Skematic.instance).thenApply {
                            Bukkit.getScheduler().runTask(Skematic.instance, Runnable {
                                it.paste(sender.location, false)
                                sender.send("<gray>Finished pasting $name.")
                            })
                        }
                    }
                    "save" -> {
                        val name = args[1].toSchematicName()
                        val pos1 = pos1s[sender]
                        val pos2 = pos2s[sender]

                        if (pos1 == null || pos2 == null) {
                            sender.send("<#cc0000>You need to set both positions first.")
                            return true
                        }

                        if (pos1s[sender]?.world != pos2s[sender]?.world) {
                            sender.send("<#cc0000>Your positions are in different worlds.")
                            return true
                        }

                        sender.send("<gray>Creating $name...")
                        Schematic.createAsync(pos1, pos2, Skematic.instance).thenApply {
                            it.saveAsync(name.toSchematic(), Skematic.instance).thenRun {
                                sender.send("<gray>Saved $name.")
                            }
                        }
                    }
                }
            }
        }

        return true
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        val completions = mutableListOf<String>()

        when (args.size) {
            1 -> completions += mutableListOf("paste", "save", "pos1", "pos2")
            2 -> {
                if (args[0].lowercase() == "paste") completions += getSchematics()
            }
        }

        return completions
    }

    private fun getSchematics(): List<String> {
        return Skematic.instance.dataFolder.listFiles()?.map { it.name } ?: emptyList()
    }

    private fun CommandSender.send(message: String) {
        sendMessage(Strings.colour(message))
    }
}