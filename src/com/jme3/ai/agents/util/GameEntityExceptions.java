package com.jme3.ai.agents.util;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class GameEntityExceptions {

    public static class GameEntityAttributeNotFound extends NullPointerException {

        public GameEntityAttributeNotFound(GameEntity gameEntity, String message) {
            super(gameEntity + " doesn't have " + message + '.');
        }
    }

    /**
     * @see NullPointerException
     * @author Tihomir Radosavljevic
     * @version 1.0.0
     */
    public static class HPSystemNotFoundException extends GameEntityAttributeNotFound {

        public HPSystemNotFoundException(GameEntity gameEntity) {
                super(gameEntity, "included HPSystem");
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
