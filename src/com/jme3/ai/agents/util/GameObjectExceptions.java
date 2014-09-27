package com.jme3.ai.agents.util;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class GameObjectExceptions {

    /**
     * @see NullPointerException
     * @author Tihomir Radosavljevic
     * @version 1.0.0
     */
    public static class HPSystemNotFoundException extends NullPointerException {

        public HPSystemNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * @see IllegalArgumentException
     * @author Jesús Martín Berlanga
     * @version 1.0.0
     */
    public static class NegativeRadiusException extends IllegalArgumentException {

        public NegativeRadiusException(String message) {
            super(message);
        }
    }
}
