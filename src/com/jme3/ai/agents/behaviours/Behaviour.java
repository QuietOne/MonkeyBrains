//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours;

import com.jme3.ai.agents.Agent;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Base class for agent behaviours.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.1
 */
public abstract class Behaviour extends AbstractControl {

    //author: Jesús Martín Berlanga
    private static class behaviourNullAgentException extends IllegalBehaviour
    {
        private  behaviourNullAgentException(String msg) { super(msg); }
    }

    /**
     * Agent to whom behaviour belongs.
     */
    protected Agent agent;

    /**
     * Constructor for behaviour that doesn't have any special spatial during
     * execution.
     * 
     * @param agent to whom behaviour belongs
     * @throws behaviourNullAgentException If agent is null
     * 
     * @author Tihomir Radosavljević
     * @author Jesús Martín Berlanga
     */
    public Behaviour(Agent agent) {
        if(agent == null) throw new behaviourNullAgentException("You can not instantiate a behaviour without an agent.");
        this.agent = agent;
        this.spatial = agent.getSpatial();
    }

    /**
     * Constructor for behaviour that has spatial during execution.
     *
     * @param agent to whom behaviour belongs
     * @param spatial which is active during execution
     * 
     * @see Behaviour#Behaviour(com.jme3.ai.agents.Agent) 
     */
    public Behaviour(Agent agent, Spatial spatial) {
        this.agent = agent;
        this.spatial = spatial;
    }
}