//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
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
            float x = 0, y = 0, z = 0;
            int distance = (int) FastMath.abs(area[1].x - area[0].x);
            if (distance > 1) {
                x = random.nextInt(distance / 2);
                if (random.nextBoolean()) {
                    x *= -1;
                }
            }

            distance = (int) FastMath.abs(area[1].z - area[0].z);
            if (distance > 1) {
                z = random.nextInt(distance / 2);
                if (random.nextBoolean()) {
                    z *= -1;
                }
            }

            distance = (int) FastMath.abs(area[1].y - area[0].y);
            if (distance > 1) {
                y = random.nextInt(distance / 2);
                if (random.nextBoolean()) {
                    y *= -1;
                }
            }

            targetPosition = new Vector3f(x, y, z);
            targetPosition.addLocal(area[0]);
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
     */
    public void setArea(Vector3f from, Vector3f to) {
        area[0] = new Vector3f(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
        area[1] = new Vector3f(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
    }
}
