package com.jme3.ai.agents.events;

import com.jme3.ai.agents.Agent;
import java.util.EventObject;

/**
 * Class that should be used for making your own Agent based events.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class AgentEvent extends EventObject {

    public AgentEvent(Agent source) {
        super(source);
    }
    
}
