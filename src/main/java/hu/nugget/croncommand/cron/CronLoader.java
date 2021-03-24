package hu.nugget.croncommand.cron;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CronLoader {

    private CronLoader() {
        throw new UnsupportedOperationException("Wtf are ya doing?");
    }

    public static List<CronTask> load(FileConfiguration config, Map<String, String> lastRun) {
        List<CronTask> tasks = new ArrayList<>();

        ConfigurationSection schedule = config.getConfigurationSection("schedule");

        if (schedule != null) {
            for (String key : schedule.getKeys(false)) {
                List<String> commands = schedule.getStringList(key);
                String lastString = lastRun.get(String.valueOf(Objects.hash(key, commands)));
                Optional<Instant> last = lastString == null
                        ? Optional.empty()
                        : Optional.ofNullable(Instant.parse(lastString));

                tasks.add(new CronTask(last, key, commands));
                Bukkit.getLogger().info("- Loading task " + tasks.get(tasks.size() - 1));
            }
        }

        return tasks;
    }

    public static void save(Path file, List<CronTask> tasks) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (CronTask task : tasks) {
                writer.write(Objects.hash(task.getCronValue(), task.getCommands()) + "IlikeTRAINS" + task.getLastRun().toString());
                writer.newLine();
            }
        }
    }
}
