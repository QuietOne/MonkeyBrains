package test;

import util.Game;
import com.jme3.scene.Spatial;
import util.Bullet;
import util.AbstractWeapon;

/**
 *
 * @author Tihomir Radosavljević
 */
public class LaserBullet extends Bullet{

    private float lifeTime;
    private float maxLifeTime = 0.25f;
    
    public LaserBullet(AbstractWeapon weapon, Spatial spatial) {
        super(weapon, spatial);
        this.lifeTime = maxLifeTime;
    }

    @Override
    public void update(float tpf) {
        //i don't know to read bytecode, so I didn't include super.update(),
        //because it probably contains something that I don't need in this demo.
        if (lifeTime <= 0 && isEnabled()) {
            lifeTime = 0;
            //here bullet property was used as means that one agent can't shoot
            //more bullets than one at moment, but it should be used to define
            //what kind of bullets are in that kind of weapon
            weapon.setBullet(null);
            Game.getInstance().removeBullet(this);
        } else {
            lifeTime -= tpf;
            }
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    
    
}
