//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.npc.steering.SteeringExceptions.WanderWithoutWanderAreaException;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.util.Random;

/**
 * This behaviour is based on a easy implementation that "generates random
 * steering force each frame, but this produces rather uninteresting motion. It
 * is 'twitchy' and produces no sustained turns. <br><br>
 *
 * Wander behaviour is steering behaviour where agent moves randomly on terrain.
 * This is done by same calculation as seek behaviour, but difference is that
 * target for this behaviour are random positions that changes durring time.
 *
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 *
 * @version 1.4.1
 */
public class WanderBehaviour extends AbstractStrengthSteeringBehaviour {

    /**
     * Position of target
     */
    protected Vector3f targetPosition;
    /**
     * Time interval durring which target position doesn't change.
     */
    protected float timeInterval;
    /**
     * Current time.
     */
    protected float time;
    /**
     * Area in which agent will wander.
     */
    protected Vector3f[] area;

    /**
     * Constructor for wander behaviour.
     *
     * @param agent to whom behaviour belongs
     */
    public WanderBehaviour(Agent agent) {
        super(agent);
        targetPosition = new Vector3f();
        velocity = new Vector3f();
        timeInterval = 2f;
        time = timeInterval;
        this.area = new Vector3f[]{Vector3f.NEGATIVE_INFINITY, Vector3f.POSITIVE_INFINITY};
    }

    /**
     * Constructor for wander behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param spatial active spatial during excecution of behaviour
     */
    public WanderBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        targetPosition = new Vector3f();
        velocity = new Vector3f();
        timeInterval = 2f;
        time = timeInterval;
        this.area = new Vector3f[]{Vector3f.NEGATIVE_INFINITY, Vector3f.POSITIVE_INFINITY};
    }

    private void validateWanderArea(Vector3f[] area) {
        if (area[0].x == area[1].x
                || area[0].y == area[1].y
                || area[0].z == area[1].z) {
            throw new WanderWithoutWanderAreaException("Components from area vectors must be different.");
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     */
    @Override
    protected Vector3f calculateRawSteering() {
        changeTargetPosition(timePerFrame);
        Vector3f desiredVelocity = targetPosition.subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        desiredVelocity.subtract(velocity);
        return desiredVelocity;
    }

    /**
     * Metod for changing target position.
     *
     * @param tpf time per frame
     */
    protected void changeTargetPosition(float tpf) {
        time -= tpf;
        if (time <= 0) {
            Random random = new Random();
            float x, up, z;
            int distance = (int) FastMath.abs(area[1].x - area[0].x);
            x = random.nextInt(distance / 2);
            if (random.nextBoolean()) {
                x *= -1;
            }
            distance = (int) FastMath.abs(area[1].z - area[0].z);
            z = random.nextInt(distance / 2);
            if (random.nextBoolean()) {
                z *= -1;
            }
            distance = (int) FastMath.abs(area[1].y - area[0].y);
            up = random.nextInt(distance / 2);
            if (random.nextBoolean()) {
                up *= -1;
            }
            targetPosition = new Vector3f(x, up, z);
            time = timeInterval;
        }
    }

    /**
     * Get time interval for changing target position.
     *
     * @return
     */
    public float getTimeInterval() {
        return timeInterval;
    }

    /**
     * Setting time interval for changing target position.
     *
     * @param timeInterval
     */
    public void setTimeInterval(float timeInterval) {
        this.timeInterval = timeInterval;
    }

    /**
     * Setting area for wander.
     *
     * @param from
     * @param to
     *
     * @throws WanderWithoutWanderAreaException If any component from the area vectors
     * match
     */
    public void setArea(Vector3f from, Vector3f to) {
        area[0] = from;
        area[1] = to;
        this.validateWanderArea(area);
    }
}
