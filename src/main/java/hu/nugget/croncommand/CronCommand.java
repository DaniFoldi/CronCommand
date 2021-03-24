package hu.nugget.croncommand;

import hu.nugget.croncommand.inject.DaggerPluginComponent;
import hu.nugget.croncommand.inject.PluginComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class CronCommand extends JavaPlugin {
    private PluginLoader loader;

    @Override
    public void onEnable() {
        final PluginComponent component = DaggerPluginComponent.builder()
                .bindPlugin(this)
                .bindConfig(getConfig())
                .bindLastrun(getLastRun())
                .build();
        this.loader = component.loader();
        this.loader.load();
    }

    @Override
    public void onDisable() {
        this.loader.unload();
    }

    private Map<String, String> getLastRun() {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(getDataFolder().toPath().resolve("lastrun.yml"))) {
            reader.lines().filter(e -> !e.trim().isEmpty()).map(e -> e.split("IlikeTRAINS")).forEach(e -> map.put(e[0], e[1]));
            return map;
        } catch (IOException e) {
            return map;
        }
    }
}