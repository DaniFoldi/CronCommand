package hu.nugget.croncommand;

import hu.nugget.croncommand.config.Configuration;
import hu.nugget.croncommand.util.Common;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Path;

public final class PluginLoader {
    private final Path datafolder;
    private final Configuration config;

    @Inject
    public PluginLoader(final @NotNull @Named("datafolder") Path datafolder,
                        final @NotNull Configuration config) {
        this.datafolder = datafolder;
        this.config = config;
    }

    protected void load() {
        try {
            this.config.populate(Common.loadConfig(Common.copyResource("config.yml", this.datafolder)));
        } catch (final IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

    protected void unload() {}
}