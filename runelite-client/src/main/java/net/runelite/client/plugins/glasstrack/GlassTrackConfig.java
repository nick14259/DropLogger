package net.runelite.client.plugins.glasstrack;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("glasstrack")
public interface GlassTrackConfig extends Config
{
    @ConfigItem(
            position = 1,
            keyName = "statTimeout",
            name = "Reset stats",
            description = "The time it takes for the current crafting session to be reset"
    )
    @Units(Units.MINUTES)
    default int statTimeout()
    {
        return 5;
    }
}
