package hu.nugget.cromcommand.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import org.jetbrains.annotations.NotNull;

public interface ObjectMappable {
    ObjectConverter CONVERTER = new ObjectConverter();

    default void populate(final @NotNull Config config) {
        CONVERTER.toObject(config, this);
    }
}
