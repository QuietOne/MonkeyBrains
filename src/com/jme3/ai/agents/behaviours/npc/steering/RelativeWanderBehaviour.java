/**
 * Copyright (c) 2014, jMonkeyEngine All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.Random;

/**
 * Wander is a type of random steering. This behaviour retain steering direction
 * state and make small random displacements to it each frame. Thus at one frame
 * the character may be turning up and to the right, and on the next frame will
 * still be turning in almost the same direction.
 *
 * @see WanderBehaviour
 *
 * @author Jesús Martín Berlanga
 * @version 1.1.0
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

            Vector3f newSteer = new Vector3f(x, y, z);
            this.targetPosition = this.targetPosition.add(newSteer.mult(this.relativeFactor)).normalize().mult(newSteer.length());
            targetPosition.addLocal(area[0]);
            time = timeInterval;
        }
    }
}