package com.danifoldi.croncommand;

import com.danifoldi.croncommand.cron.CronRunner;
import com.danifoldi.croncommand.inject.DaggerPluginComponent;
import com.danifoldi.croncommand.inject.PluginComponent;
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