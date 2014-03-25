package com.jme3.ai.agents.behaviours.player;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.List;

/**
 * Simple attackBehaviour for player controled agent. It has support for
 * AnalogListener and ActionListener.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimplePlayerAttackBehaviour extends Behaviour implements ActionListener, AnalogListener {

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
    }

    @Override
    protected void controlUpdate(float tpf) {
        supportedOperations.get(operation).update(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("You should override it youself");
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        operation = name;
        if (isPressed) {
            enabled = true;
        } else {
            enabled = false;
        }
        //TODO: see if other paramethers can be used
    }

    public void onAnalog(String name, float value, float tpf) {
        operation = name;
        //TODO: see if other paramethers can be used
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
