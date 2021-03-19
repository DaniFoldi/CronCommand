package hu.nugget.croncommand.inject;

import dagger.BindsInstance;
import dagger.Component;
import hu.nugget.croncommand.CronCommand;
import hu.nugget.croncommand.PluginLoader;

import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
@Component(modules = ProvidingModule.class)
public interface PluginComponent {

    PluginLoader loader();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder bindPlugin(final CronCommand plugin);

        @BindsInstance
        Builder bindDatafolder(final @Named("datafolder") Path datafolder);

        PluginComponent build();
    }
}