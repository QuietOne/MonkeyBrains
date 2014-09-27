//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.Random;

/**
 * "Wander is a type of random steering." This behaviour "retain steering
 * direction state and make small random displacements to it each frame. Thus at
 * one frame the character may be turning up and to the right, and on the next
 * frame will still be turning in almost the same direction."
 *
 * @see WanderBehaviour
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.0
 */
public class RelativeWanderBehaviour extends WanderBehaviour {

    private float relativeFactor;

    /**
     * @param relativeFactor How much should differ each new wander force ? A
     * value near to 0 means that each new force must differ slightly from the
     * previous one.
     * @see WanderBehaviour#WanderBehaviour(com.jme3.ai.agents.Agent)
     */
    public RelativeWanderBehaviour(Agent agent, Vector3f from, Vector3f to, float relativeFactor) {
        super(agent);
        super.setArea(from, to);
        this.relativeFactor = relativeFactor;
    }

    /**
     * @see
     * RelativeWanderBehaviour#RelativeWanderBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.math.Vector3f, com.jme3.math.Vector3f, float)
     * @see WanderBehaviour#WanderBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public RelativeWanderBehaviour(Agent agent, Vector3f from, Vector3f to, float relativeFactor, Spatial spatial) {
        super(agent, spatial);
        super.setArea(from, to);
        this.relativeFactor = relativeFactor;
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     */
    @Override
    protected Vector3f calculateRawSteering() {
        changeTargetPosition(timePerFrame);
        return targetPosition;
    }

    /**
     * @see WanderBehaviour#changeTargetPosition(float)
     */
    @Override
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

            Vector3f newSteer = new Vector3f(x, up, z);
            this.targetPosition = this.targetPosition.add(newSteer.mult(this.relativeFactor)).normalize().mult(newSteer.length());
            time = timeInterval;
        }
    }
}