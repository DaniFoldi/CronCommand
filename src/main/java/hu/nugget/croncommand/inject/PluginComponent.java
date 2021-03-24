package hu.nugget.croncommand.inject;

import dagger.BindsInstance;
import dagger.Component;
import hu.nugget.croncommand.CronCommand;
import hu.nugget.croncommand.PluginLoader;
import hu.nugget.croncommand.cron.CronRunner;

import javax.inject.Singleton;

@Singleton
@Component
public interface PluginComponent {

    PluginLoader loader();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder bindPlugin(final CronCommand plugin);

        @BindsInstance
        Builder bindCronRunner(final CronRunner cronRunner);

        PluginComponent build();
    }
}