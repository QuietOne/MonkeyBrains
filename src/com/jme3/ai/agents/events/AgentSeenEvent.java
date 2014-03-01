package com.jme3.ai.agents.events;

import com.jme3.ai.agents.Agent;

/**
 * Event that is being activated when agent is seen.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class AgentSeenEvent extends AgentEvent {
    /**
     * Information about Agent that have been seen by the source.
     */
    private Agent agentSeen;

    public AgentSeenEvent(Agent source, Agent agentSeen) {
        super(source);
        this.agentSeen = agentSeen;
    }

    public Agent getAgentSeen() {
        return agentSeen;
    }
}
