package com.jme3.ai.agents.util.weapons;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 2.0.0
 */
public abstract class AbstractWeapon extends GameObject {

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
     * How much time is needed for next attack.
     */
    protected float cooldown;
    /**
     * Time used for calculating cooldown of weapons.
     */
    private float timeSinceFired = 0;

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
     * Check if enemy agent or game object of interest to AI is in range of
     * weapon.
     *
     * @param target
     * @return
     */
    public boolean isInRange(GameObject target) {
        return isInRange(target.getLocalTranslation());
    }

    @Override
    protected void controlUpdate(float tpf) {
        timeSinceFired -= tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Method for inquiry is weapon in cooldown.
     *
     * @return false - if weapon can attack, true otherwise
     */
    public boolean isInCooldown() {
        return timeSinceFired > 0;
    }

    /**
     * Reset cooldown so the weapon can be used again.
     */
    public void resetCooldown() {
        timeSinceFired = 0;
    }

    /**
     * Method for setting weapon's cooldown to maximum.
     */
    protected void setFullCooldown() {
        timeSinceFired = cooldown;
    }

    /**
     * Method for calculating if the requirements have been met for the weapon
     * being used. (Is there ammo, etc)
     *
     * Note: Do not check cooldown, it is already checked by this framework.
     *
     * @return true if usable, false otherwise
     */
    public abstract boolean isUsable();

    public abstract void attack(Vector3f targetPosition, float tpf);
    
    /**
     * Method for creating bullets and setting them to move.
     *
     * @param target
     * @param tpf time per frame
     */
    public void attack(GameObject target, float tpf) {
        attack(target.getLocalTranslation(), tpf);
    }

    /**
     * Method for checking if there is unlimited use of the weapon.
     *
     * @return true - unlimited use, false otherwise
     */
    protected abstract boolean isUnlimitedUse();

    /**
     * Method for decreasing stats of weapon after usage. (ammo, mana, hp etc.)
     */
    protected abstract void useWeapon();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
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

    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
}
