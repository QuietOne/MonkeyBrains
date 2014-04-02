package com.jme3.ai.agents.events;

import java.util.EventListener;

/**
 * Interface for listeners for PhysicalObjectSeenEvent.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public interface PhysicalObjectSeenListener extends EventListener {

    /**
     * How listener should handle this type of event.
     *
     * @param event
     */
    public void handlePhysicalObjectSeenEvent(PhysicalObjectSeenEvent event);
}
