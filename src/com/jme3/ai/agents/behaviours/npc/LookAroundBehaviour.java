package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.ai.agents.events.AgentSeenEvent;
import com.jme3.ai.agents.events.AgentSeenEventListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import com.jme3.ai.agents.util.Game;

/**
 * Example of looking behaviour for NPC.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class LookAroundBehaviour extends Behaviour {
    
    /**
     * List of listeners to which behaviours AgentSeenEvent should forward to.
     * If more than one type of Events one behaviour forwards, then use EventListenerList.
     * @see EventListenerList
     */
    private List<AgentSeenEventListener> listeners;
    
    public LookAroundBehaviour(Agent agent) {
        super(agent);
        listeners = new ArrayList<AgentSeenEventListener>();
    }
    
    public void addListener(AgentSeenEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(AgentSeenEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method for calling all behaviours that are affected by what agent is seeing.
     * @param agentSeen Agent that have been seen
     */
    private void fireAgentSeenEvent(Agent agentSeen) {
        //create AgentSeenEvent
        AgentSeenEvent event = new AgentSeenEvent(agent, agentSeen);
        //forward it to all listeners
        for (AgentSeenEventListener listener : listeners) {
            listener.handleAgentSeenEvent(event);
        }
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        List<Agent> agents = Game.getInstance().viewPort(agent, FastMath.QUARTER_PI);
        for (int i = 0; i < agents.size(); i++) {
            //I wanted them to join against me
            //if you want to have teams, than add in model team identificator
            //and check it here, if you want that every agent for itself then
            //just delete this condition
            if (agents.get(i).getName().equals("Player")) {
                fireAgentSeenEvent(agents.get(i));
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }
    
}
