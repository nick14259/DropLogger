package net.runelite.client.plugins.droplogger;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;


@PluginDescriptor(
        name = "DropLogger",
        description = "A plugin to log drops and the killcount they were received on.",
        tags = {"drops"},
        loadWhenOutdated = true,
        enabledByDefault = false
)
@Slf4j
public class DropLoggerPlugin extends Plugin {
    @Inject
    private DropLoggerConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    private DropLoggerPanel panel;
    private NavigationButton navButton;

    @Override
    protected void startUp() throws Exception{
        log.info("Plugin has started.");
        panel = injector.getInstance(DropLoggerPanel.class);
        panel.init(config);

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "droplog_icon.png");

        navButton = NavigationButton.builder().tooltip("DropLogger").icon(icon).priority(9).panel(panel).build();
        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception{
        log.info("Plugin has stopped.");
        clientToolbar.removeNavigation(navButton);
    }

    @Provides
    DropLoggerConfig getConfig(ConfigManager configManager){
        return configManager.getConfig(DropLoggerConfig.class);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged){
        if(gameStateChanged.getGameState() == GameState.LOGIN_SCREEN){
            if (config.getShouldLog()) {
                System.out.println("Plugin is on");
            } else {
                System.out.println("Plugin is off");
            }
        }
    }

    public boolean TestRun(){
        if (config.getShouldLog()) {
            System.out.println("Plugin is on");
            return true;
            // Do something here when enabled/true
        } else {
            System.out.println("Plugin is off");
            return false;
            // Do something else when disabled/false
        }
    }





}
