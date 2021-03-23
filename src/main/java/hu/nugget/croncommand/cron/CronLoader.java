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
                String lastString = lastRun == null ? null : lastRun.getString(String.valueOf(Objects.hash(key, commands)));
                Optional<Instant> last = lastString == null
                        ? Optional.empty()
                        : Optional.ofNullable(Instant.parse(lastString));

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
