//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
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
 * Jesús Martín Berlanga: "You can flee another agent or a specific space
 * location".
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.2
 */
public class FleeBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Agent from whom we flee.
     */
    private Agent target;
    private Vector3f fleePos;

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

    /**
     * Constructor for flee behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param fleePos position from that we flee
     */
    public FleeBehaviour(Agent agent, Vector3f fleePos) {
        super(agent);
        this.fleePos = fleePos;
    }

    /**
     * Constructor for flee behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param fleePos position from that we flee
     * @param spatial active spatial during excecution of behaviour
     */
    public FleeBehaviour(Agent agent, Vector3f fleePos, Spatial spatial) {
        super(agent, spatial);
        this.fleePos = fleePos;
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
    @Override
    protected Vector3f calculateFullSteering() {
        Vector3f desiredVelocity;

        if (this.target != null) {
            desiredVelocity = target.getLocalTranslation().subtract(agent.getLocalTranslation());
        } else if (this.fleePos != null) {
            desiredVelocity = this.fleePos.subtract(agent.getLocalTranslation());
        } else {
            return new Vector3f(); //We do not have any target or flee position
        }
        Vector3f aVelocity = this.agent.getVelocity();

        if (aVelocity == null) {
            aVelocity = new Vector3f();
        }

        return desiredVelocity.subtract(aVelocity).negate();
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
        this.fleePos = null;
    }

    public Vector3f getFleePos() {
        return this.fleePos;
    }

    public void setFleePos(Vector3f fleePos) {
        this.fleePos = fleePos;
        this.target = null;
    }
}
