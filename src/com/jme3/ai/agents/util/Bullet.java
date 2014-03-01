package com.jme3.ai.agents.util;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Spatial;

/**
 *
 * @author Tihomir Radosavljević
 */
public class Bullet extends BulletAppState {
    
    //does it really have to have reference to weapon from which is fired
    protected AbstractWeapon weapon;
    protected Spatial spatial;

    public Bullet(AbstractWeapon weapon, Spatial spatial) {
        this.weapon = weapon;
        this.spatial = spatial;
    }

    public AbstractWeapon getWeapon() {
        return weapon;
    }

    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }
    
}
