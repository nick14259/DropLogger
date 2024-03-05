package net.runelite.client.plugins.glasstrack;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;

class GlassTrackSession
{
    @Getter(AccessLevel.PACKAGE)
    private int productsMade;

    @Getter(AccessLevel.PACKAGE)
    private int moltenGlassMade;

    @Getter(AccessLevel.PACKAGE)
    private Instant lastItemMade;

    void increaseProductsMade()
    {
        productsMade++;
        lastItemMade = Instant.now();
    }

    void increaseMoltenGlassMade()
    {
        moltenGlassMade++;
        lastItemMade = Instant.now();
    }
}