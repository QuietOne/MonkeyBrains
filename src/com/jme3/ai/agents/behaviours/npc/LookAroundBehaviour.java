package behaviours.npc;

import agents.Agent;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import events.AgentSeenEvent;
import events.AgentSeenEventListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import util.Game;

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
            fireAgentSeenEvent(agents.get(i));
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }
    
}
