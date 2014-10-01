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
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Slows down the velocity produced by a behaviour container (g.e.
 * CompoundSteeringBehaviour)
 *
 * @see CompoundSteeringBehaviour
 * @see com.jme3.ai.agents.behaviours.npc.SimpleMainBehaviour
 *
 * @author Jesús Martín Berlanga
 * @version 2.1
 */
public class SlowBehaviour extends AbstractStrengthSteeringBehaviour {

    private int timeInterval;
    private float slowPercentage;
    private float maxBrakingFactor = 1;
    private ActionListener slowIteration = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            float newStrength = getBrakingFactorWrapper() * (1 - slowPercentage);

            if (newStrength < maxBrakingFactor) {
                setBrakingFactorWrapper(newStrength);
            }
        }
    };
    private Timer iterationTimer;

    /**
     * Slows a steer behaviour resultant velocity.
     *
     * @param behaviour Steer behaviour
     * @param timeInterval How much time for each slow iteration in ns
     * @param percentajeSlow What percentaje will be reduced the vecocity for
     * each iteration, a float betwen 0 and 1
     *
     * @throws SteeringExceptions.NegativeValueException If time interval is not
     * a positive integer
     *
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public SlowBehaviour(Agent agent, int timeInterval, float percentajeSlow) {
        super(agent);
        this.construct(timeInterval, percentajeSlow);
    }

    /**
     * @see SlowBehaviour#SlowBehaviour(com.jme3.ai.agents.Agent, int, float)
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public SlowBehaviour(Agent agent, int timeInterval, float percentajeSlow, Spatial spatial) {
        super(agent, spatial);
        this.construct(timeInterval, percentajeSlow);
    }

    /**
     * @param slowPercentage float in the interval [0, 1]
     */
    public void setSlowPercentage(float slowPercentage) {
        //Auto adjust invalid inputs
        if (slowPercentage > 1) {
            this.slowPercentage = 1;
        } else if (slowPercentage < 0) {
            this.slowPercentage = 0;
        } else {
            this.slowPercentage = slowPercentage;
        }
    }

    private float getBrakingFactorWrapper() {
        return this.getBrakingFactor();
    }

    private void setBrakingFactorWrapper(float brakingFacor) {
        this.setBrakingFactor(brakingFacor);
    }

    /**
     * Turns on or off the slow behaviour
     *
     * @param active
     */
    public void setAcive(boolean active) {
        if (active && !this.iterationTimer.isRunning()) {
            this.iterationTimer.start();
        } else if (!active && this.iterationTimer.isRunning()) {
            this.iterationTimer.stop();
        }
    }

    /**
     * Reset the slow behaviour
     */
    public void reset() {
        this.setBrakingFactor(1);
    }

    private void construct(int timeInterval, float percentajeSlow) {
        if (timeInterval <= 0) {
            throw new SteeringExceptions.NegativeValueException("The time interval must be positive.", timeInterval);
        }

        this.timeInterval = timeInterval;

        if (percentajeSlow > 1) {
            this.slowPercentage = 1;
        } else if (percentajeSlow < 0) {
            this.slowPercentage = 0;
        } else {
            this.slowPercentage = percentajeSlow;
        }

        this.iterationTimer = new Timer(this.timeInterval, this.slowIteration);
    }

    @Override
    protected Vector3f calculateRawSteering() {
        return Vector3f.ZERO;
    }

    public void setMaxBrakingFactor(float maxBrakingFactor) {
        this.maxBrakingFactor = maxBrakingFactor;
    }
}
