package hu.nugget.cromcommand;

import hu.nugget.cromcommand.inject.DaggerPluginComponent;
import hu.nugget.cromcommand.inject.PluginComponent;
import org.bukkit.plugin.java.JavaPlugin;

public class CronCommand extends JavaPlugin {
    private PluginLoader loader;

    @Override
    public void onEnable() {
        final PluginComponent component = DaggerPluginComponent.builder()
                .bindPlugin(this)
                .bindDatafolder(getDataFolder().toPath())
                .build();
        this.loader = component.loader();
        this.loader.load();
    }

    @Override
    public void onDisable() {
        this.loader.unload();
    }
}