package com.jme3.ai.agents.util.weapons;

import com.jme3.ai.agents.util.control.Game;
import com.jme3.math.Vector3f;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public abstract class AbstractBulletBasedWeapon extends AbstractWeapon {

    /**
     * One weapon have one type of bullet. Each fired bullet can be in it's own
     * update().
     */
    protected AbstractBullet bullet;

    public AbstractBullet getBullet() {
        return bullet;
    }

    public void setBullet(AbstractBullet bullet) {
        this.bullet = bullet;
    }

    /**
     * Method for creating bullets and setting them to move.
     *
     * @param direction direction of bullets to go
     * @param tpf time per frame
     */
    public void attack(Vector3f direction, float tpf) {
        //does weapon have bullets
        if (!isUsable()) {
            return;
        }
        //is weapon in cooldown
        if (isInCooldown()) {
            return;
        }
        //fire bullet
        AbstractBullet firedBullet = controlAttack(direction, tpf);
        if (firedBullet != null) {
            //if there is bullet than add it to be updated regulary in game
            Game.getInstance().addGameObject(firedBullet);
        }
        //set weapon cooldown
        setFullCooldown();
        //decrease number of bullets if weapon have limited number of bullets
        if (!isUnlimitedUse()) {
            useWeapon();
        }
    }

    /**
     * Setting bullet that should be fired and giving it initial velocity.
     *
     * @param direction
     * @param tpf
     * @return
     */
    protected abstract AbstractBullet controlAttack(Vector3f direction, float tpf);
}
