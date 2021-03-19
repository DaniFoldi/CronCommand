package hu.nugget.croncommand.cron;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CronLoader {

    private static String DEFAULT_INSTANT_STRING = "1970-01-01T00:00:00.000000Z";

    public List<CronTask> load(FileConfig config) {
        List<CronTask> tasks = new ArrayList<>();

        Config schedule = config.get("schedule");
        Config lastRun = config.get("lastrun");

        for (Config.Entry e: schedule.entrySet()) {
            String cron = e.getKey();
            List<String> commands = e.getRawValue();
            Instant last = Instant.parse(lastRun == null ? DEFAULT_INSTANT_STRING : lastRun.getOrElse(String.valueOf(Objects.hash(cron, commands)), DEFAULT_INSTANT_STRING));
            tasks.add(new CronTask(last, cron, commands));
        }

        return tasks;
    }

    public void save(FileConfig config, List<CronTask> tasks) {
        for (CronTask task: tasks) {
            String key = "lastrun." + Objects.hash(task.getCronValue(), task.getCommands());
            if (config.contains(key)) {
                config.set(key, task.getLastRun().toString());
            } else {
                config.add(key, task.getLastRun().toString());
            }
        }

        config.save();
    }
}
