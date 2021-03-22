package hu.nugget.croncommand.cron;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import hu.nugget.croncommand.CronCommand;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CronRunner {

    private final List<CronTask> tasks;

    private BukkitTask timer = null;

    private static final CronParser PARSER;

    static {
        PARSER = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
    }

    public CronRunner(List<CronTask> tasks) {
        this.tasks = tasks;
    }

    public void start(CronCommand plugin) {
        timer = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            for (CronTask task: tasks) {
                Cron cronTask = PARSER.parse(task.getCronValue());
                ExecutionTime time = ExecutionTime.forCron(cronTask);
                ZonedDateTime zonedTime = task.getLastRun().atZone(ZoneId.systemDefault());
                Optional<ZonedDateTime> lastTime = time.lastExecution(ZonedDateTime.now());

                if (lastTime.isPresent() && lastTime.get().isAfter(zonedTime)) {
                    plugin.getLogger().info("Executing commands for cron " + task.getCronValue());
                    for (String command: task.getCommands()) {
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                    }
                    task.setLastRun(lastTime.get().toInstant());
                }
            }
        }, 0, 20);
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public List<CronTask> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }
}
