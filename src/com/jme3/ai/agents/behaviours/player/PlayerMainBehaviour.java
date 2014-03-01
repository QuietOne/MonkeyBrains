package com.jme3.ai.agents.behaviours.player;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Example of mainBehaviour for player.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class PlayerMainBehaviour extends Behaviour{

    /**
     * Attacking behaviour for player.
     */
    private PlayerAttackBehaviour attackBehaviour;
    /**
     * Moving behaviour for player.
     */
    private PlayerMoveBehaviour moveBehaviour;
    
    /**
     * @param agent agent to whom is added this behaviour
     * @param cam camera added to this agent
     */
    public PlayerMainBehaviour(Agent agent, Camera cam, float terrainSize) {
        super(agent);
        //TODO: add some spatial and attach while shooting
        attackBehaviour = new PlayerAttackBehaviour(agent, null, cam);
        moveBehaviour = new PlayerMoveBehaviour(agent, null, terrainSize);
        attackBehaviour.setEnabled(false);
        moveBehaviour.setEnabled(false);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        attackBehaviour.update(tpf);
        moveBehaviour.update(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

    /**
     * I need this for registering Inputs in Main class.
     * @return attackBehaviour
     */
    public PlayerAttackBehaviour getAttackBehaviour() {
        return attackBehaviour;
    }

    /**
     * I need this for registering Inputs in Main class.
     * @return moveBehaviour
     */
    public PlayerMoveBehaviour getMoveBehaviour() {
        return moveBehaviour;
    }
}
