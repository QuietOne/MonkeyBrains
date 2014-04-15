package com.jme3.ai.agents.events;

import java.util.EventObject;

/**
 * Base event for all PhysicalObjectEvents.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class GameObjectEvent extends EventObject {

    /**
     * Constructor for GameObjectEvent
     *
     * @param source GameObject that have produced this Event
     */
    public GameObjectEvent(Object source) {
        super(source);
    }
}
