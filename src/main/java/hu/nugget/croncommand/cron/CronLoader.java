package hu.nugget.croncommand.cron;

import hu.nugget.croncommand.CronCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CronLoader {

    private CronLoader() {
        throw new UnsupportedOperationException();
    }

    public static List<CronTask> load(CronCommand plugin) {
        plugin.reloadConfig();
        ConfigurationSection schedule = plugin.getConfig().getConfigurationSection("schedule");
        Map<String, String> lastRun = getLastRun(plugin);

        List<CronTask> tasks = new ArrayList<>();

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

    private static Map<String, String> getLastRun(CronCommand plugin) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(plugin.getDataFolder().toPath().resolve("lastrun.yml"))) {
            reader.lines().filter(e -> !e.trim().isEmpty()).map(e -> e.split("IlikeTRAINS")).forEach(e -> map.put(e[0], e[1]));
            return map;
        } catch (IOException e) {
            return map;
        }
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
