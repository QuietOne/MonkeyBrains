//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Purpose of seek behaviour is to steer agent towards a specified position or
 * object. Basically it's movement behaviour.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.2
 */
public class SeekBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Agent whom we seek.
     */
    private Agent target;

    /**
     * Constructor for seek behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent whom we seek
     */
    public SeekBehaviour(Agent agent, Agent target) {
        super(agent);
        this.target = target;
    }

    /**
     * Constructor for seek behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent from we seek
     * @param spatial active spatial during excecution of behaviour
     */
    public SeekBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, spatial);
        this.target = target;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     * 
     * @see AbstractStrengthSteeringBehaviour#calculateFullSteering() 
     * 
     * @author Tihomir Radosavljević
     * @author Jesús Martín Berlanga
     */
    protected Vector3f calculateFullSteering() {
        Vector3f desiredVelocity = target.getLocalTranslation().subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        Vector3f aVelocity = this.agent.getVelocity();
        
        if(aVelocity == null)
            aVelocity = new Vector3f();
        
        return desiredVelocity.subtract(aVelocity);
    }
    
    /**
     * Get agent from we seek.
     *
     * @return agent
     */
    public Agent getTarget() {
        return target;
    }

    /**
     * Setting agent from we seek.
     *
     * @param target
     */
    public void setTarget(Agent target) {
        this.target = target;
    }
}
