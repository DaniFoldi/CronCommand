package hu.nugget.croncommand.cron;

import hu.nugget.croncommand.CronCommand;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CronRunner {

    private List<CronTask> tasks;

    private BukkitTask timer = null;

    public CronRunner() {
    }

    public void setTasks(List<CronTask> tasks) {
        this.tasks = tasks;
    }

    public void start(CronCommand plugin) {
        timer = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            for (CronTask task : tasks) {
                Optional<Instant> lastTime = CronParser.lastExecutionTime(task.getCronValue());

                if (lastTime.isEmpty() || lastTime.get().isAfter(task.getLastRun())) {
                    plugin.getLogger().info("Executing commands for cron " + task.getCronValue());
                    for (String command : task.getCommands()) {
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                    }
                    task.setLastRun(lastTime.get());
                }
            }
        }, 0, 20);
    }

    public void cancel() {
        setTasks(Collections.emptyList());
        if (timer != null) {
            timer.cancel();
        }
    }

    public List<CronTask> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }
}
