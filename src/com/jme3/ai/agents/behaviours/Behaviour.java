//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.BehaviourExceptions.AgentNotIncluded;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Base class for agent behaviours.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.2.1
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
     * @throws AgentNotIncluded if agent is null
     */
    public Behaviour(Agent agent) {
        if (agent == null) {
            throw new BehaviourExceptions.AgentNotIncluded();
        }
        this.agent = agent;
        this.spatial = agent.getSpatial();
    }

    /**
     * Constructor for behaviour that has spatial during execution.
     *
     * @param agent to whom behaviour belongs
     * @param spatial which is active during execution
     * @see Behaviour#Behaviour(com.jme3.ai.agents.Agent)
     * @throws AgentNotIncluded if agent is null
     */
    public Behaviour(Agent agent, Spatial spatial) {
        if (agent == null) {
            throw new BehaviourExceptions.AgentNotIncluded();
        }
        this.agent = agent;
        this.spatial = spatial;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { 
    }
}
