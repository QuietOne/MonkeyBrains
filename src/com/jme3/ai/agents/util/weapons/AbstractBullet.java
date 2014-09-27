package com.jme3.ai.agents.util.weapons;

import com.jme3.ai.agents.util.GameObject;
import com.jme3.scene.Spatial;

/**
 * Base class for bullets in game.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public abstract class AbstractBullet extends GameObject {

    /**
     * Weapon from which bullet was fired.
     */
    protected AbstractFirearmWeapon weapon;

    /**
     * Constructor for AbstractBullet.
     *
     * @param weapon weapon from which bullet was fired
     * @param spatial spatial for bullet
     */
    public AbstractBullet(AbstractFirearmWeapon weapon, Spatial spatial) {
        this.weapon = weapon;
        this.spatial = spatial;
    }

    /**
     * Get weapon.
     *
     * @return weapon from which bullet was fired
     */
    public AbstractFirearmWeapon getWeapon() {
        return weapon;
    }

    /**
     * Setting weapon.
     *
     * @param weapon weapon from which bullet was fired
     */
    public void setWeapon(AbstractFirearmWeapon weapon) {
        this.weapon = weapon;
    }
}
