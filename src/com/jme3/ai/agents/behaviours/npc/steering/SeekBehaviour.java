//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Purpose of seek behaviour is to steer agent towards a specified position or
 * object. Basically it's movement behaviour. <br> <br>
 *
 * Jesús Martín Berlanga: "You can seek another agent or a specific space
 * location".
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.5
 */
public class SeekBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Agent whom we seek.
     */
    private Agent target;
    private Vector3f seekingPos;

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
     * @param seekingPos position that we seek
     * @param spatial active spatial during excecution of behaviour
     */
    public SeekBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, spatial);
        this.target = target;
    }

    /**
     * Constructor for seek behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param seekingPos position that we seek
     *
     * @author Jesús Martín Berlanga
     */
    public SeekBehaviour(Agent agent, Vector3f seekingPos) {
        super(agent);
        this.seekingPos = seekingPos;
    }

    /**
     * Constructor for seek behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent from we seek
     * @param spatial active spatial during excecution of behaviour
     *
     * @author Jesús Martín Berlanga
     */
    public SeekBehaviour(Agent agent, Vector3f seekingPos, Spatial spatial) {
        super(agent, spatial);
        this.seekingPos = seekingPos;
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     *
     * @see AbstractStrengthSteeringBehaviour#calculateRawSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        Vector3f desiredVelocity;

        if (this.target != null) {
            desiredVelocity = target.getLocalTranslation().subtract(agent.getLocalTranslation());
        } else if (this.seekingPos != null) {
            desiredVelocity = this.seekingPos.subtract(agent.getLocalTranslation()).normalize();
        } else {
            return new Vector3f(); //We do not have a target or position to seek
        }
        Vector3f aVelocity = this.agent.getVelocity();

        if (aVelocity == null) {
            aVelocity = new Vector3f();
        }

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
        this.seekingPos = null;
    }

    public Vector3f getSeekingPos() {
        return this.seekingPos;
    }

    public void setSeekingPos(Vector3f seekingPos) {
        this.seekingPos = seekingPos;
        this.target = null;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
