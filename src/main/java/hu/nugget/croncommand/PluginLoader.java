package hu.nugget.croncommand;

import hu.nugget.croncommand.cron.CronLoader;
import hu.nugget.croncommand.cron.CronRunner;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

public final class PluginLoader {
    private final CronCommand plugin;
    private final FileConfiguration config;
    private final Map<String, String> lastRun;
    private CronRunner cronRunner;
    private ReloadCommand reloadCommand;

    @Inject
    public PluginLoader(final @NotNull CronCommand plugin,
                        final @NotNull FileConfiguration config,
                        final @NotNull Map<String, String> lastRun) {
        this.plugin = plugin;
        this.config = config;
        this.lastRun = lastRun;
    }

    protected void load() {
        this.plugin.saveDefaultConfig();
        this.cronRunner = new CronRunner(CronLoader.load(this.config, this.lastRun));
        this.plugin.getLogger().info("Loaded " + this.cronRunner.getTasks().size() + " tasks");
        this.cronRunner.start(this.plugin);
        this.reloadCommand = new ReloadCommand(config, this);
        Optional.ofNullable(this.plugin.getCommand("croncommand")).ifPresent(c -> c.setExecutor(reloadCommand));
    }

    protected void unload() {
        this.cronRunner.cancel();
        try {
            if (!Files.exists(this.plugin.getDataFolder().toPath().resolve("lastrun.yml"))) {
                Files.createFile(this.plugin.getDataFolder().toPath().resolve("lastrun.yml"));
            }
            CronLoader.save(this.plugin.getDataFolder().toPath().resolve("lastrun.yml"), this.cronRunner.getTasks());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}