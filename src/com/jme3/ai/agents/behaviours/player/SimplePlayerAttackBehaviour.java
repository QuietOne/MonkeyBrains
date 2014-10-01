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
package com.jme3.ai.agents.behaviours.player;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.SimpleAttackBehaviour;
import com.jme3.ai.agents.util.control.AIAppState;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.List;

/**
 * Simple attackBehaviour for player controled agent. It has support for
 * ActionListener.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimplePlayerAttackBehaviour extends Behaviour implements ActionListener {

    /**
     * Operation that should be done.
     */
    private String operation;
    /**
     * Supported operation for this agent base on inputs.
     */
    private HashMap<String, Behaviour> supportedOperations;

    /**
     * Constructor for SimplePlayerAttackBehaviour.
     *
     * @param agent to whom behaviour belongs
     * @param spatial active spatial during excecution of behaviour
     */
    public SimplePlayerAttackBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        supportedOperations = new HashMap<String, Behaviour>();
        enabled = true;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (operation == null) {
            return;
        }
        supportedOperations.get(operation).update(tpf);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        operation = name;
        if (isPressed) {
            supportedOperations.get(operation).setEnabled(true);
            Vector2f click2d = AIAppState.getInstance().getApp().getInputManager().getCursorPosition();
            Vector3f click3d = agent.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = agent.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            Plane ground = new Plane(Vector3f.UNIT_Y, 0);
            Vector3f groundpoint = new Vector3f();
            ray.intersectsWherePlane(ground, groundpoint);
            ((SimpleAttackBehaviour) supportedOperations.get(operation)).setTarget(groundpoint);
        } else {
            operation = null;
        }
    }

    /**
     * Add supported operations to this behaviour.
     *
     * @param name of supported operation
     * @param operation behaviour that should be done
     */
    public void addSuportedOperation(String name, Behaviour operation) {
        supportedOperations.put(name, operation);
    }

    /**
     * Add supported operations to this behaviour.
     *
     * @param names list of supported operations
     * @param operations list of behaviours that should be done
     * @return if sizes of names and operations don't match, true otherwise
     */
    public boolean addSuportedOperations(List<String> names, List<Behaviour> operations) {
        if (names.size() != operations.size()) {
            return false;
        }
        for (int i = 0; i < operations.size(); i++) {
            supportedOperations.put(names.get(i), operations.get(i));
        }
        return true;
    }

    /**
     * Add supported operations to this behaviour.
     *
     * @param supportedOperations map of supported operations
     */
    public void addSupportedOperations(HashMap<String, Behaviour> supportedOperations) {
        this.supportedOperations.putAll(supportedOperations);
    }

    /**
     * Get supported operations.
     *
     * @return
     */
    public HashMap<String, Behaviour> getSupportedOperations() {
        return supportedOperations;
    }

    /**
     * Setting supported operations.
     *
     * @param supportedActions
     */
    public void setSupportedOperations(HashMap<String, Behaviour> supportedActions) {
        this.supportedOperations = supportedActions;
    }
}
