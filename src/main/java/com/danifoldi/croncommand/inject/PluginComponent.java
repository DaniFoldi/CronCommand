package com.danifoldi.croncommand.inject;

import com.danifoldi.croncommand.CronCommand;
import com.danifoldi.croncommand.cron.CronRunner;
import com.danifoldi.croncommand.PluginLoader;
import dagger.BindsInstance;
import dagger.Component;

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