package com.jme3.ai.agents.util;

import com.jme3.ai.agents.util.control.Game;
import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Abstract class for defining weapons used by agents.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class AbstractWeapon extends PhysicalObject {

    /**
     * Name of weapon.
     */
    protected String name;
    /**
     * Name of agent to whom weapon is added.
     */
    protected Agent agent;
    /**
     * Maximum range of weapon.
     */
    protected float maxAttackRange;
    /**
     * Minimum range of weapon.
     */
    protected float minAttackRange = 0;
    /**
     * Attack damage of weapon.
     */
    protected float attackDamage;
    /**
     * Number of bullet that this weapon have left.
     */
    protected int numberOfBullets;
    /**
     * One weapon have one type of bullet. Each fired bullet can be in it's own
     * update().
     */
    protected AbstractBullet bullet;
    /**
     * How much time is needed for next attack.
     */
    protected float cooldown;
    /**
     * Time used for calculating cooldown of weapons.
     */
    private float timeSinceFired = 0;

    /**
     * Check if enemy agent is in range of weapon.
     *
     * @param target
     * @return
     */
    public boolean isInRange(PhysicalObject target) {
        if (agent.getLocalTranslation().distance(target.getLocalTranslation()) > maxAttackRange) {
            return false;
        }
        if (agent.getLocalTranslation().distance(target.getLocalTranslation()) < minAttackRange) {
            return false;
        }
        return true;
    }

    /**
     * Check if target position is in range of weapon.
     *
     * @param targetPosition
     * @return
     */
    public boolean isInRange(Vector3f targetPosition) {
        if (agent.getLocalTranslation().distance(targetPosition) > maxAttackRange) {
            return false;
        }
        if (agent.getLocalTranslation().distance(targetPosition) < minAttackRange) {
            return false;
        }
        return true;
    }

    /**
     * Check if there is any bullets in weapon.
     *
     * @return true - has, false - no, it doesn't
     */
    public boolean hasBullets() {
        if (numberOfBullets == 0) {
            return false;
        }
        return true;
    }

    /**
     * Method for creating bullets and setting them to move.
     *
     * @param direction direction of bullets to go
     * @param tpf time per frame
     */
    public void attack(Vector3f direction, float tpf) {
        //does weapon have bullets
        if (!hasBullets()) {
            return;
        }
        //is weapon in cooldown
        if (timeSinceFired > 0) {
            return;
        }
        //fire bullet
        AbstractBullet firedBullet = controlAttack(direction, tpf);
        if (firedBullet != null) {
            //if there is bullet than add it to be updated regulary in game
            Game.getInstance().addGameObject(firedBullet);
        }
        //set weapon cooldown
        timeSinceFired = cooldown;
        //decrease number of bullets if weapon have limited number of bullets
        if (numberOfBullets != -1) {
            numberOfBullets--;
        }
    }

    /**
     * Method for creating bullets and setting them to move.
     *
     * @param target
     * @param tpf time per frame
     */
    public void attack(PhysicalObject target, float tpf) {
        attack(target.getLocalTranslation(), tpf);
    }

    protected abstract AbstractBullet controlAttack(Vector3f direction, float tpf);

    public String getName() {
        return name;
    }

    public Agent getAgent() {
        return agent;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public int getNumberOfBullets() {
        return numberOfBullets;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setNumberOfBullets(int numberOfBullets) {
        this.numberOfBullets = numberOfBullets;
    }

    public void addNumberOfBullets(int numberOfBullets) {
        this.numberOfBullets += numberOfBullets;
    }

    public AbstractBullet getBullet() {
        return bullet;
    }

    public void setBullet(AbstractBullet bullet) {
        this.bullet = bullet;
    }

    public float getMaxAttackRange() {
        return maxAttackRange;
    }

    public void setMaxAttackRange(float maxAttackRange) {
        this.maxAttackRange = maxAttackRange;
    }

    public float getMinAttackRange() {
        return minAttackRange;
    }

    public void setMinAttackRange(float minAttackRange) {
        this.minAttackRange = minAttackRange;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    protected void controlUpdate(float tpf) {
        timeSinceFired -= tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
