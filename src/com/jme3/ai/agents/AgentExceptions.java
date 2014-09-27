package com.jme3.ai.agents;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class AgentExceptions {

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
    
    public static class TeamNotFoundExecption extends NullPointerException {

        public TeamNotFoundExecption(Agent agent) {
            super("Agent "+ agent.getName()+ " doesn't have a designated team.");
        }
        
    }
}
