//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Simple move behaviour: Agent moves in the "moveDirection" direction.
 *
 * @autor Jesús Martín Berlanga
 * @version 1.1.1
 */
public class MoveBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Move direction of agent.
     */
    protected Vector3f moveDirection;

    /**
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public MoveBehaviour(Agent agent) {
        super(agent);
    }

    /**
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public MoveBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Vector3f getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(Vector3f moveDirection) {
        this.moveDirection = moveDirection.normalize();
    }

    /**
     * @see AbstractStrengthSteeringBehaviour#calculateRawSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        Vector3f steer = new Vector3f();

        if (this.moveDirection != null) {
            steer = this.moveDirection;
        }

        return steer;
    }
}