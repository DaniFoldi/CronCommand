package hu.nugget.croncommand.cron;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

public class CronParser {
    private static final com.cronutils.parser.CronParser PARSER;

    static {
        PARSER = new com.cronutils.parser.CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
    }

    public static Optional<Instant> lastExecutionTime(String cronString) {
        Cron cronTask = PARSER.parse(cronString);
        Optional<ZonedDateTime> dateTime = ExecutionTime.forCron(cronTask).lastExecution(ZonedDateTime.now());
        if (dateTime.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(dateTime.get().toInstant());
    }

    private CronParser() {
        throw new UnsupportedOperationException();
    }
}
