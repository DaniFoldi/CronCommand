package hu.nugget.croncommand;

import hu.nugget.croncommand.cron.CronLoader;
import hu.nugget.croncommand.cron.CronRunner;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

public final class PluginLoader {
    private final CronCommand plugin;
    private final FileConfiguration config;
    private CronRunner cronRunner;
    private ReloadCommand reloadCommand;

    @Inject
    public PluginLoader(final @NotNull CronCommand plugin,
                        final @NotNull FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    protected void load() {
        this.plugin.saveDefaultConfig();
        this.cronRunner = new CronRunner(CronLoader.load(this.config));
        this.cronRunner.start(this.plugin);
        this.reloadCommand = new ReloadCommand(config, this);
        Optional.ofNullable(this.plugin.getCommand("croncommand")).ifPresent(c -> c.setExecutor(reloadCommand));
    }

    protected void unload() {
        this.cronRunner.cancel();
        try {
            CronLoader.save(this.plugin.getDataFolder().toPath().resolve("config.yml"),
                    this.config, this.cronRunner.getTasks());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}