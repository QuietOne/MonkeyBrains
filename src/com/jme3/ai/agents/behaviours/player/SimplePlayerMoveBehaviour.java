package com.jme3.ai.agents.behaviours.player;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.SimpleMoveBehaviour;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.List;

/**
 * Simple moveBehaviour for player controled agent. It has support for
 * AnalogListener.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimplePlayerMoveBehaviour extends SimpleMoveBehaviour implements AnalogListener {

    /**
     * Name of operation that should be done.
     */
    private String operation;
    /**
     * Supported operation for this agent base on inputs.
     */
    private HashMap<String, Behaviour> supportedOperations;

    /**
     * Constructor for SimplePlayerMoveBehaviour.
     *
     * @param agent Agent to whom is added this behaviour
     * @param spatial active spatial durring moving
     */
    public SimplePlayerMoveBehaviour(Agent agent, Spatial spatial) {
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

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void onAnalog(String name, float value, float tpf) {
        operation = name;
        if (value != 0) {
            supportedOperations.get(operation).setEnabled(true);
            controlUpdate(tpf);
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