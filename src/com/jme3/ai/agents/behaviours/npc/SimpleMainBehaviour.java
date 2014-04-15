package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.ai.agents.util.control.Game;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple main behaviour for NPC. Main behaviour contains other Behaviours and
 * if active it will update all behavioour that are enabled.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
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
     * @see Game#over
     */
    protected Game game;

    /**
     * This behaviour never have spatial.
     *
     * @param agent
     */
    public SimpleMainBehaviour(Agent agent) {
        //Main behaviour doesn't have need for spatials.
        super(agent);
        game = Game.getInstance();
        behaviours = new LinkedList<Behaviour>();
        enabled = true;
    }

    @Override
    protected void controlUpdate(float tpf) {
        for (Behaviour behaviour : behaviours) {
            behaviour.update(tpf);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
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