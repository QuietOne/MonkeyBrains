package com.jme3.ai.agents.behaviours;

import com.jme3.ai.agents.Agent;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Base class for agent behaviours.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class Behaviour extends AbstractControl {

    /**
     * Agent to whom behaviour belongs.
     */
    protected Agent agent;

    /**
     * Constructor for behaviour that doesn't have any special spatial during
     * execution.
     *
     * @param agent to whom behaviour belongs
     */
    public Behaviour(Agent agent) {
        this.agent = agent;
    }

    /**
     * Constructor for behaviour that has spatial during execution.
     *
     * @param agent to whom behaviour belongs
     * @param spatial which is active during execution
     */
    public Behaviour(Agent agent, Spatial spatial) {
        this.agent = agent;
        this.spatial = spatial;
    }
}
