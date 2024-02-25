package net.runelite.client.plugins.droplogger;

// Mandatory imports
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;


@ConfigGroup("droplogger")
public interface DropLoggerConfig extends Config {

    @ConfigItem (
            keyName = "shouldLog",
            name = "Should Log",
            description = "Checkbox to turn logging on and off."
    )
    default boolean getShouldLog()
    {
        return false;
    }


}
