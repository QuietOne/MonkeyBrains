package com.jme3.ai.agents.events;

import java.util.EventListener;

/**
 * Interface for listeners for GameObjectSeenEvent.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public interface GameObjectSeenListener extends EventListener {

    /**
     * How listener should handle this type of event.
     *
     * @param event
     */
    public void handleGameObjectSeenEvent(GameObjectSeenEvent event);
}
