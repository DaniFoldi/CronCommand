package hu.nugget.croncommand.inject;

import dagger.Module;
import dagger.Provides;
import hu.nugget.croncommand.config.Configuration;

import javax.inject.Singleton;

@Module
public class ProvidingModule {

    @Provides
    @Singleton
    public Configuration provideConfiguration() {
        return new Configuration();
    }
}