//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Flee is simply the inverse of seek and acts to steer the agent so that its
 * velocity is radially aligned away from the target. The desired velocity
 * points in the opposite direction.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.1
 */
public class FleeBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Agent from whom we flee.
     */
    private Agent target;

    /**
     * Constructor for flee behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent from whom we flee
     */
    public FleeBehaviour(Agent agent, Agent target) {
        super(agent);
        this.target = target;
    }

    /**
     * Constructor for flee behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent from whom we flee
     * @param spatial active spatial during excecution of behaviour
     */
    public FleeBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, spatial);
        this.target = target;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     * 
     * @see AbstractStrengthSteeringBehaviour#calculateFullSteering() 
     */
    protected Vector3f calculateFullSteering() {
        Vector3f desiredVelocity = target.getLocalTranslation().subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        return desiredVelocity.subtract(velocity).negate();
    }

    /**
     * Get agent from whom we flee.
     *
     * @return agent
     */
    public Agent getTarget() {
        return target;
    }

    /**
     * Setting agent from whom we flee.
     *
     * @param target
     */
    public void setTarget(Agent target) {
        this.target = target;
    }
}
