package net.runelite.client.plugins.glasstrack;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.glasstrack.GlassTrackConfig;
import net.runelite.client.plugins.glasstrack.GlassTrackOverlay;
import net.runelite.client.plugins.glasstrack.GlassTrackSession;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "GlassTrack",
        description = "Show glassblowing information",
        tags = {"overlay", "skilling"}
)
@PluginDependency(XpTrackerPlugin.class)
public class GlassTrackPlugin extends Plugin
{
    @Inject
    private GlassTrackConfig config;

    @Inject
    private net.runelite.client.plugins.glasstrack.GlassTrackOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private GlassTrackSession session;

    private int moltenGlassMade;

    @Provides
    GlassTrackConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(GlassTrackConfig.class);
    }

    @Override
    protected void startUp()
    {
        session = null;
        overlayManager.add(overlay);
        moltenGlassMade = 0;
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        session = null;
        moltenGlassMade = 0;
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        if (event.getType() != ChatMessageType.SPAM)
        {
            return;
        }

        if (event.getMessage().startsWith("You make a "))
        {
            if (session == null)
            {
                session = new GlassTrackSession();
            }
            session.increaseProductsMade();
        }
        else if (event.getMessage().startsWith("You heat the sand "))
        {
            session.increaseMoltenGlassMade();
        }

    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (session != null)
        {
            final Duration statTimeout = Duration.ofMinutes(config.statTimeout());
            final Duration sinceCaught = Duration.between(session.getLastItemMade(), Instant.now());

            if (sinceCaught.compareTo(statTimeout) >= 0)
            {
                session = null;
            }
        }
    }
}