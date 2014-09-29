package com.jme3.ai.agents.behaviours;

import com.jme3.ai.agents.Agent;

/**
 * Class container for exceptions related to behaviours.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public class BehaviourExceptions {

    /**
     * This exception is thrown if it has been tried to instantiate a behaviour
     * with illegal arguments. <br><br>
     *
     * This prevent the developer from suffer unexpected situations.
     *
     * @see IllegalBehaviourException
     * @author Jesús Martín Berlanga
     * @author Tihomir Radosavljević
     * @version 1.1.0
     */
    public static class IllegalBehaviourException extends IllegalArgumentException {

        public IllegalBehaviourException(String message) {
            super(message);
        }

        public IllegalBehaviourException(Agent agent, String message, float value) {
            super(agent + message + value + '.');
        }
    }

    /**
     * @see IllegalBehaviourException
     * @author Jesús Martín Berlanga
     * @author Tihomir Radosavljević
     * @version 1.1.0
     */
    public static class AgentNotIncluded extends IllegalBehaviourException {

        public AgentNotIncluded() {
            super("You can not instantiate a behaviour without an agent.");
        }
    }

    /**
     * This exception is thrown if the program tries to call behaviour that has
     * not been initialized.
     *
     * @see NullPointerException
     * @author Tihomir Radosaljević
     * @version 1.0.0
     */
    public static class NullBehaviourException extends NullPointerException {

        public NullBehaviourException() {
            super("Behaviour has not been initialized");
        }

        public NullBehaviourException(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown if the behaviour tries to call null target.
     *
     * @see IllegalStateException
     * @author Tihomir Radosaljević
     * @version 1.0.0
     */
    public static class TargetNotFound extends IllegalStateException {

        public TargetNotFound() {
            super("Target can not be null");
        }
    }
}
