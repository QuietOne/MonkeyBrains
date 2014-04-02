package com.jme3.ai.agents.util;

import com.jme3.scene.Spatial;

/**
 * Base class for bullets in game.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class AbstractBullet extends PhysicalObject {

    /**
     * Weapon from which bullet was fired.
     */
    protected AbstractWeapon weapon;

    /**
     * Constructor for AbstractBullet.
     *
     * @param weapon weapon from which bullet was fired
     * @param spatial spatial for bullet
     */
    public AbstractBullet(AbstractWeapon weapon, Spatial spatial) {
        this.weapon = weapon;
        this.spatial = spatial;
    }

    /**
     * Get weapon.
     *
     * @return weapon from which bullet was fired
     */
    public AbstractWeapon getWeapon() {
        return weapon;
    }

    /**
     * Setting weapon.
     *
     * @param weapon weapon from which bullet was fired
     */
    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }
}
