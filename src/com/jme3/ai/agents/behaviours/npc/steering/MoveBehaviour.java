//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Simple move behaviour for NPC. Agent should move  to moveDirection. 
 *
 * @see Agent#moveSpeed
 *
 * @autor Jesús Martín Berlanga
 * @version 1.1
 */
public class MoveBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Move direction of agent.
     */
    protected Vector3f moveDirection;


    public MoveBehaviour(Agent agent) {
        super(agent);
    }

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

    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering()  */
    @Override
    Vector3f calculateFullSteering() {
        
        Vector3f moveDirection = new Vector3f();

        if(this.moveDirection != null)
            moveDirection = this.moveDirection;

        return moveDirection;
    }
}
