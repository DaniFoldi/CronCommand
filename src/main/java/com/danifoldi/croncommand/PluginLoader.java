package com.danifoldi.croncommand;

import com.danifoldi.croncommand.cron.CronLoader;
import com.danifoldi.croncommand.cron.CronRunner;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class PluginLoader {
    private final CronCommand plugin;
    private final CronRunner cronRunner;
    private ReloadCommand reloadCommand;

    @Inject
    public PluginLoader(final @NotNull CronCommand plugin,
                        final @NotNull CronRunner cronRunner) {
        this.plugin = plugin;
        this.cronRunner = cronRunner;
    }

    protected void load() {
        this.plugin.saveDefaultConfig();
        cronRunner.setTasks(CronLoader.load(this.plugin));
        this.plugin.getLogger().info("Loaded " + this.cronRunner.getTasks().size() + " tasks");
        this.cronRunner.start(this.plugin);
        this.reloadCommand = new ReloadCommand(plugin.getConfig(), this);
        Optional.ofNullable(this.plugin.getCommand("croncommand")).ifPresent(c -> c.setExecutor(reloadCommand));
    }

    protected void unload() {
        this.cronRunner.cancel();
        try {
            Path lastRunFile = this.plugin.getDataFolder().toPath().resolve("lastrun.txt");
            if (!Files.exists(lastRunFile)) {
                Files.createFile(lastRunFile);
            }
            CronLoader.save(lastRunFile, this.cronRunner.getTasks());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}