package com.jme3.ai.agents.util.weapons;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class WeaponExceptions {

    public static class WeaponNotFound extends NullPointerException{

        public WeaponNotFound(String message) {
            super(message);
        }

    }
}
