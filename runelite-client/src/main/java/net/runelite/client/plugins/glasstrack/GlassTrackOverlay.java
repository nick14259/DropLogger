package net.runelite.client.plugins.glasstrack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

import net.runelite.api.Client;

import static net.runelite.api.AnimationID.*;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import net.runelite.api.Skill;
import net.runelite.client.plugins.glasstrack.GlassTrackPlugin;
import net.runelite.client.plugins.glasstrack.GlassTrackSession;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class GlassTrackOverlay extends OverlayPanel
{
    private static final int CRAFT_TIMEOUT = 7;
    private static final String CRAFTING_RESET = "Reset";

    private final Client client;
    private final GlassTrackPlugin plugin;
    private final XpTrackerService xpTrackerService;

    @Inject
    GlassTrackOverlay(Client client, GlassTrackPlugin plugin, XpTrackerService xpTrackerService)
    {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.xpTrackerService = xpTrackerService;
        setPosition(OverlayPosition.TOP_LEFT);
        addMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "GlassTrack overlay");
        addMenuEntry(RUNELITE_OVERLAY, CRAFTING_RESET, "GlassTrack overlay", e -> plugin.setSession(null));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        GlassTrackSession session = plugin.getSession();
        if (session == null)
        {
            return null;
        }

        if (isCrafting() || Duration.between(session.getLastItemMade(), Instant.now()).getSeconds() < CRAFT_TIMEOUT)
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Glassblowing")
                    .color(Color.GREEN)
                    .build());
        }
        else
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("NOT glassblowing")
                    .color(Color.RED)
                    .build());
        }

        int actions = xpTrackerService.getActions(Skill.CRAFTING);
        if (actions > 0)
        {
            if (plugin.getSession().getProductsMade() > 0)
            {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Products:")
                        .right(Integer.toString(session.getProductsMade()))
                        .build());
            }
            if (plugin.getSession().getMoltenGlassMade() > 0)
            {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Molten Glass:")
                        .right(Integer.toString(session.getMoltenGlassMade()))
                        .build());
            }
            if (actions > 2)
            {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Actions/hr:")
                        .right(Integer.toString(xpTrackerService.getActionsHr(Skill.CRAFTING)))
                        .build());
            }
        }

        return super.render(graphics);

    }

    private boolean isCrafting()
    {
        switch (client.getLocalPlayer().getAnimation())
        {
            case CRAFTING_GLASSBLOWING:
            case MAGIC_LUNAR_GEOMANCY:
                return true;
            default:
                return false;
        }
    }
}