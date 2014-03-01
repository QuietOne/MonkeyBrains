package com.jme3.ai.agents.behaviours.npc.neural;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.LookAroundBehaviour;
import com.jme3.ai.agents.behaviours.npc.MoveBehaviour;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.ai.agents.events.AgentSeenEvent;
import com.jme3.ai.agents.events.AgentSeenEventListener;
import com.jme3.ai.agents.util.Game;

/**
 * One behaviour to rule them all.<br>
 * Behaviour that use behaviours that have implemented neural network within themselves
 * to learn proper behaviour activation.
 * 
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class NeuralMainBehaviour extends Behaviour implements AgentSeenEventListener {

    private LookAroundBehaviour lookAroundBehaviour;
    private MoveBehaviour moveBehaviour;
    /**
     * Attack behaviour that calculates next position of agent and shoots there.
     */
    private NeuralAttackBehaviour neuralAttackBehaviour;
    
    
    public NeuralMainBehaviour(Agent agent, float terrainSize) {
        //Main behaviour doesn't have need for spatials.
        super(agent);
        lookAroundBehaviour = new LookAroundBehaviour(agent);
        //TODO: put some spatial to move behaviour
        moveBehaviour = new MoveBehaviour(agent, null, terrainSize);
        //TODO: put some spatial to attack behaviour
        neuralAttackBehaviour = new NeuralAttackBehaviour(agent, null);
        lookAroundBehaviour.addListener(moveBehaviour);
        lookAroundBehaviour.addListener(this);
        moveBehaviour.setEnabled(true);
        neuralAttackBehaviour.setEnabled(false);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        Game game = Game.getInstance();
        if (agent.isAlive() && !game.isOver()) {
            lookAroundBehaviour.setEnabled(true);
        } else {
            lookAroundBehaviour.setEnabled(false);
        }
        lookAroundBehaviour.update(tpf);
        moveBehaviour.update(tpf);
        neuralAttackBehaviour.update(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

    public void handleAgentSeenEvent(AgentSeenEvent event) {
        Agent target = event.getAgentSeen();
        //if in range
        if (agent.getWeapon().isInRange(target)) {
            neuralAttackBehaviour.setTarget(target);
            //attack mode enabled
            neuralAttackBehaviour.setEnabled(true);
        }
    }

}
