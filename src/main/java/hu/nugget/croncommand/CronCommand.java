package hu.nugget.croncommand;

import hu.nugget.croncommand.cron.CronLoader;
import hu.nugget.croncommand.cron.CronRunner;
import hu.nugget.croncommand.cron.CronTask;
import hu.nugget.croncommand.inject.DaggerPluginComponent;
import hu.nugget.croncommand.inject.PluginComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CronCommand extends JavaPlugin {
    private PluginLoader loader;
    private List<CronTask> tasks;
    private CronRunner cronRunner;

    @Override
    public void onEnable() {
        final PluginComponent component = DaggerPluginComponent.builder()
                .bindPlugin(this)
                .bindDatafolder(getDataFolder().toPath())
                .build();
        this.loader = component.loader();
        this.loader.load();

        this.tasks = new CronLoader().load(loader.getConfig());
        this.cronRunner = new CronRunner(this.tasks);

        cronRunner.start(this);
    }

    @Override
    public void onDisable() {
        cronRunner.cancel();
        new CronLoader().save(loader.getConfig(), this.tasks);
        this.loader.unload();
    }
}