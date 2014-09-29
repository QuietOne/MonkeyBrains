package com.jme3.ai.agents.events;

import java.util.EventListener;

/**
 * Interface for listeners for GameEntitySeenEvent.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public interface GameEntitySeenListener extends EventListener {

    /**
     * How listener should handle this type of event.
     *
     * @param event
     */
    public void handleGameEntitySeenEvent(GameEntitySeenEvent event);
}
