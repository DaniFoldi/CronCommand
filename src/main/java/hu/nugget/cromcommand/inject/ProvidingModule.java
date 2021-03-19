package hu.nugget.cromcommand.inject;

import dagger.Module;
import dagger.Provides;
import hu.nugget.cromcommand.config.Configuration;

import javax.inject.Singleton;

@Module
public class ProvidingModule {

    @Provides
    @Singleton
    public Configuration provideConfiguration() {
        return new Configuration();
    }
}