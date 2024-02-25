package net.runelite.client.plugins.droplogger;

import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;



@PluginDescriptor(
        name = "DropLogger",
        description = "A plugin to log drops and the killcount they were received on.",
        tags = {"drops"},
        loadWhenOutdated = true,
        enabledByDefault = false
)

public class DropLoggerPlugin {
    @Inject
    private DropLoggerConfig config;

    if (config.shouldLog()) {
        System.out.println("Plugin is on");
        // Do something here when enabled/true
    } else {
        System.out.println("Plugin is off");
        // Do something else when disabled/false
    }


}
