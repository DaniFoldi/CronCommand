package hu.nugget.croncommand.cron;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CronLoader {

    private static final String DEFAULT_INSTANT_STRING = "1970-01-01T00:00:00.000000Z";

    private CronLoader() {
        throw new UnsupportedOperationException("Wtf are ya doing?");
    }

    public static List<CronTask> load(FileConfiguration config) {
        List<CronTask> tasks = new ArrayList<>();

        ConfigurationSection schedule = config.getConfigurationSection("schedule");
        ConfigurationSection lastRun = config.getConfigurationSection("lastrun");

        if (schedule != null) {
            for (String key : schedule.getKeys(false)) {
                List<String> commands = schedule.getStringList(key);
                Instant last = Instant.parse(lastRun == null
                        ? DEFAULT_INSTANT_STRING
                        : Optional.ofNullable(lastRun.getString(String.valueOf(Objects.hash(key, commands))))
                        .orElse(DEFAULT_INSTANT_STRING));
                tasks.add(new CronTask(last, key, commands));
            }
        }

        return tasks;
    }

    public static void save(Path file, FileConfiguration config, List<CronTask> tasks) throws IOException {
        for (CronTask task : tasks) {
            String key = "lastrun." + Objects.hash(task.getCronValue(), task.getCommands());
            config.set(key, task.getLastRun().toString());
        }

        config.save(file.toFile());
    }
}
