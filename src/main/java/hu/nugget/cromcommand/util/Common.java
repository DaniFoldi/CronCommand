package hu.nugget.cromcommand.util;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import hu.nugget.cromcommand.CronCommand;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Common {

    private Common() {
        throw new UnsupportedOperationException("No instances for you :P");
    }

    public static @NotNull Path copyResource(final @NotNull String name, final @NotNull Path parent) throws IOException {
        final Path dest = parent.resolve(name);
        if (Files.notExists(parent)) {
            Files.createDirectories(parent);
        }

        try (final InputStream stream = CronCommand.class.getResourceAsStream("/" + name)) {
            Files.copy(stream, dest);
        }

        return dest;
    }

    public static @NotNull Config loadConfig(final @NotNull Path file) {
        final FileConfig config = FileConfig.of(file);
        config.load();

        return config;
    }
}