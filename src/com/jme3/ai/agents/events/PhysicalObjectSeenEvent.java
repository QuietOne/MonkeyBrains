package com.jme3.ai.agents.events;

import com.jme3.ai.agents.util.PhysicalObject;

/**
 * Event for seen PhysicalObjects.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class PhysicalObjectSeenEvent extends PhysicalObjectEvent {

    /**
     * PhysicalObject that have been seen.
     */
    private PhysicalObject physicalObjectSeen;

    /**
     *
     * @param source object that produce this event (it is usually agent)
     * @param physicalObject PhysicalObject that have been seen
     */
    public PhysicalObjectSeenEvent(Object source, PhysicalObject physicalObject) {
        super(source);
        this.physicalObjectSeen = physicalObject;
    }

    /**
     *
     * @return seen PhysicalObject
     */
    public PhysicalObject getPhysicalObjectSeen() {
        return physicalObjectSeen;
    }

    /**
     *
     * @param physicalObjectSeen seen PhysicalObject
     */
    public void setPhysicalObjectSeen(PhysicalObject physicalObjectSeen) {
        this.physicalObjectSeen = physicalObjectSeen;
    }
}
