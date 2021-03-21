package hu.nugget.croncommand.inject;

import dagger.BindsInstance;
import dagger.Component;
import hu.nugget.croncommand.CronCommand;
import hu.nugget.croncommand.PluginLoader;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
@Component
public interface PluginComponent {

    PluginLoader loader();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder bindPlugin(final CronCommand plugin);

        @BindsInstance
        Builder bindConfig(final FileConfiguration config);

        PluginComponent build();
    }
}