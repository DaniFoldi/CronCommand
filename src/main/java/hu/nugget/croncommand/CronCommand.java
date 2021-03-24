package hu.nugget.croncommand;

import hu.nugget.croncommand.cron.CronRunner;
import hu.nugget.croncommand.inject.DaggerPluginComponent;
import hu.nugget.croncommand.inject.PluginComponent;
import org.bukkit.plugin.java.JavaPlugin;

public class CronCommand extends JavaPlugin {
    private PluginLoader loader;

    @Override
    public void onEnable() {
        final PluginComponent component = DaggerPluginComponent.builder()
                .bindPlugin(this)
                .bindCronRunner(new CronRunner())
                .build();
        this.loader = component.loader();
        this.loader.load();
    }

    @Override
    public void onDisable() {
        this.loader.unload();
    }
}