package events;

import java.util.EventListener;

/**
 * Interface for all behaviours that listens to AgentSeenEvent.
 * @see AgentSeenEvent
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public interface AgentSeenEventListener extends EventListener {
    public void handleAgentSeenEvent(AgentSeenEvent event);
}
