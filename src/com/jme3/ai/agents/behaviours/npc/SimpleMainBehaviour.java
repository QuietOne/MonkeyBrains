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
package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.util.control.AIAppState;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple main behaviour for NPC. Main behaviour contains other Behaviours and
 * if active it will update all behavioour that are enabled. <br> <br>
 * You can only add one steer behaviour to this container. But you can use
 * CompoundSteeringBehaviour to merge more steer behaviours into one.
 *
 * @author Tihomir Radosavljević
 * @version 1.1.1
 */
public class SimpleMainBehaviour extends Behaviour {

    /**
     * Behaviours are implemented as LinkedList for flexibility. If there isn't
     * changing behaviours while agent is active you can change it to ArrayList
     * for speed.
     *
     * @see ArrayList
     * @see LinkedList
     */
    protected List<Behaviour> behaviours;
    /**
     * Instance of game. Main behaviour will not work if game is over.
     *
     * @see AIAppState#over
     */
    protected AIAppState game;

    /**
     * This behaviour never have spatial.
     *
     * @param agent
     */
    public SimpleMainBehaviour(Agent agent) {
        //Main behaviour doesn't have need for spatials.
        super(agent);
        game = AIAppState.getInstance();
        behaviours = new LinkedList<Behaviour>();
        enabled = true;
    }

    @Override
    protected void controlUpdate(float tpf) {
        for (Behaviour behaviour : behaviours) {
            behaviour.update(tpf);
        }
    }

    /**
     * Remove all behaviours from this behaviour.
     */
    public void clearBehaviours() {
        behaviours.clear();
    }

    /**
     * Set list of bahaviours for this behaviour to do.
     *
     * @param behaviours
     */
    public void setBehaviours(List<Behaviour> behaviours) {
        this.behaviours = behaviours;
    }

    /**
     * Add behaviour to this main behaviour.
     *
     * @param behaviour that will be added
     */
    public void addBehaviour(Behaviour behaviour) {
        behaviours.add(behaviour);
    }

    /**
     * Remove behaviour from this main behaviour.
     *
     * @param behaviour that will be removed
     */
    public void removeBehaviour(Behaviour behaviour) {
        behaviours.remove(behaviour);
    }
}
