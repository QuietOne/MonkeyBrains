package com.jme3.ai.agents.behaviours;

/**
 * Class container for exceptions related to steering.
 *
 * @author Tihomir Radosavljevic
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
     * @version 1.0.2
     */
    public static class IllegalBehaviourException extends IllegalArgumentException {

        public IllegalBehaviourException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalBehaviourException
     * @author Jesús Martín Berlanga
     * @version 1.0.1
     */
    public static class NullAgentException extends IllegalBehaviourException {

        public NullAgentException() {
            super("You can not instantiate a behaviour without an agent.");
        }
    }

    /**
     * This exception is thrown if the program tries to call behaviour that has
     * not been initialized.
     *
     * @see NullPointerException
     * @author Tihomir Radosaljevic
     * @version 1.0.0
     */
    public static class NullBehaviourException extends NullPointerException {

        public NullBehaviourException(){
            super("Behaviour has not been initialized");
        }
        
        public NullBehaviourException(String message) {
            super(message);
        }
    }
    
    public static class TargetNotFound extends IllegalBehaviourException {

        public TargetNotFound() {
            super("Target can not be null");
        }
        
    }
}
