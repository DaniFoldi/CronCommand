package hu.nugget.croncommand;

import com.electronwill.nightconfig.core.file.FileConfig;
import hu.nugget.croncommand.util.Common;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Path;

public final class PluginLoader {
    private final Path datafolder;
    private FileConfig config;

    @Inject
    public PluginLoader(final @NotNull @Named("datafolder") Path datafolder) {
        this.datafolder = datafolder;
    }

    protected void load() {
        try {
            this.config = Common.loadConfig(Common.copyResource("config.yml", this.datafolder));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void unload() {
        config.close();
    }

    public FileConfig getConfig() {
        return config;
    }
}