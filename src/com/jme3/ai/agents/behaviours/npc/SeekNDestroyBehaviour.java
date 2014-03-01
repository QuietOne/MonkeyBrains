package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.ai.agents.events.AgentSeenEvent;
import com.jme3.ai.agents.events.AgentSeenEventListener;
import com.jme3.ai.agents.util.Game;

/**
 * Example of mainBehaviour for NPC.
 * @author Tihomir Radosavljević
 */
public class SeekNDestroyBehaviour extends Behaviour implements AgentSeenEventListener{

    private LookAroundBehaviour lookAroundBehaviour;
    private MoveBehaviour moveBehaviour;
    private AttackBehaviour attackBehaviour;
    
    
    public SeekNDestroyBehaviour(Agent agent, float terrainSize) {
        //Main behaviour doesn't have need for spatials.
        super(agent);
        lookAroundBehaviour = new LookAroundBehaviour(agent);
        //TODO: put some spatial to move behaviour
        moveBehaviour = new MoveBehaviour(agent, null, terrainSize);
        //TODO: put some spatial to attack behaviour
        attackBehaviour = new AttackBehaviour(agent, null);
        lookAroundBehaviour.addListener(moveBehaviour);
        lookAroundBehaviour.addListener(this);
        moveBehaviour.setEnabled(true);
        attackBehaviour.setEnabled(false);
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
        attackBehaviour.update(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering    
    }

    public void handleAgentSeenEvent(AgentSeenEvent event) {
        Agent target = event.getAgentSeen();
        if (agent.getWeapon().isInRange(target)) {
            attackBehaviour.setTarget(target);
            attackBehaviour.setEnabled(enabled);
        }
    }

}
