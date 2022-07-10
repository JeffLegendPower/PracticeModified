package me.trixxtraxx.Practice.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class DeepConfig(val plugin: JavaPlugin) {

    init {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdir()
        }
    }

    fun loadConfig(configName: String): FileConfiguration {
        plugin.saveResource("$configName.yml", false)

        return YamlConfiguration.loadConfiguration(File(
            plugin.dataFolder
            , "$configName.yml"
        ))
    }

    fun createBlankConfig(name: String, template: FileConfiguration?): FileConfiguration {
        val config = YamlConfiguration()
        if (template != null) {
            config.defaults = template
            config.options().copyDefaults(true)
        }
        config.save(File(plugin.dataFolder, "$name.yml"))
        return config
    }

    fun getConfig(name: String): FileConfiguration {
        return YamlConfiguration.loadConfiguration(File(
            plugin.dataFolder
            , "$name.yml"
        ))
    }

    fun resetConfig(name: String) {
        plugin.saveResource("$name.yml", true)
    }
}