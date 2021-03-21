package hu.nugget.croncommand.cron;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class CronTask {

    private Instant lastRun;
    private final String cronValue;
    private final List<String> commands;

    public CronTask(Instant lastRun, String cronValue, List<String> commands) {
        this.lastRun = lastRun;
        this.cronValue = cronValue;
        this.commands = commands;
    }

    public Instant getLastRun() {
        return lastRun;
    }

    public void setLastRun(Instant instant) {
        lastRun = instant;
    }

    public String getCronValue() {
        return cronValue;
    }

    public List<String> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public String toString() {
        return "CronTask{" + this.lastRun + ", " + this.cronValue + "," + this.commands + "}";
    }
}
