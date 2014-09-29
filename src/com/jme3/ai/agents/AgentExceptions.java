package com.jme3.ai.agents;

import com.jme3.ai.agents.util.GameEntityExceptions;

/**
 *
 * Class container for exceptions related to agent.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public class AgentExceptions {

    /**
     * This exception is thrown If it has been tried to instantiate an agent
     * with illegal arguments. <br><br>
     *
     * This prevent the developer from suffer unexpected situations.
     *
     * @author Jesús Martín Berlanga
     * @version 1.0.0
     */
    public static class IllegalAgentException extends IllegalArgumentException {

        public IllegalAgentException(String message) {
            super(message);
        }
    }

    public static class AgentAttributeNotFound extends GameEntityExceptions.GameEntityAttributeNotFound {

        public AgentAttributeNotFound(Agent agent, String message) {
            super(agent, message);
        }
    }

    public static class TeamNotFoundException extends AgentAttributeNotFound {

        public TeamNotFoundException(Agent agent) {
            super(agent, "a designated team");
        }
    }

    public static class InventoryNotFoundException extends AgentAttributeNotFound {

        public InventoryNotFoundException(Agent agent) {
            super(agent, "an inventory");
        }
    }

    public static class WeaponNotFoundException extends AgentAttributeNotFound {

        public WeaponNotFoundException(Agent agent) {
            super(agent, "a weapon");
        }
    }

    /**
     *
     * @see IllegalArgumentException
     * @author Jesús Martín Berlanga
     * @author 1.0.0
     */
    public static class InvalidNeighborhoodIDistanceException extends IllegalArgumentException {

        public InvalidNeighborhoodIDistanceException(String message) {
            super(message);
        }
    }
}
