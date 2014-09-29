package com.jme3.ai.agents.events;

import java.util.EventObject;

/**
 * Base event for all GameEntityEvents.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public class GameEntityEvent extends EventObject {

    /**
     * Constructor for GameEntityEvent
     *
     * @param source GameEntity that have produced this Event
     */
    public GameEntityEvent(Object source) {
        super(source);
    }
}
