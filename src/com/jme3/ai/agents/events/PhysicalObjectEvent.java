package com.jme3.ai.agents.events;

import java.util.EventObject;

/**
 * Base event for all PhysicalObjectEvents.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class PhysicalObjectEvent extends EventObject {

    /**
     * Constructor for PhysicalObjectEvent
     *
     * @param source GameObject that have produced this Event
     */
    public PhysicalObjectEvent(Object source) {
        super(source);
    }
}
