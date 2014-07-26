package com.jme3.ai.agents.behaviours.player;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.SimpleAttackBehaviour;
import com.jme3.ai.agents.util.control.Game;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.List;

/**
 * Simple attackBehaviour for player controled agent. It has support for
 * ActionListener.
 *
 * @author Tihomir RadosavljeviÄ
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

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        operation = name;
        if (isPressed) {
            supportedOperations.get(operation).setEnabled(true);
            Vector2f click2d = Game.getInstance().getApp().getInputManager().getCursorPosition();
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