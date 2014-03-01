package com.jme3.ai.agents.util;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Abstract class for defining weapons used by agents.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class AbstractWeapon {
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
     * Spatial for weapon.
     */
    protected Spatial spatial;
    /**
     * One weapon have one type of bullet. Each fired bullet can be in it's own
     * update().
     */
    protected Bullet bullet;

    /**
     * Check if enemy agent is in range of weapon.
     * @param target
     * @return 
     */
    public boolean isInRange(Agent target) {
        if (agent.getLocalTranslation().distance(target.getLocalTranslation()) > maxAttackRange) {
            return false;
        }
        if (agent.getLocalTranslation().distance(target.getLocalTranslation()) < minAttackRange) {
            return false;
        }
        return true;
    }

    /**
     * Check if there is any bullets in weapon.
     * @return true - has, false - no, it doesn't
     */
    public boolean hasBullets(){
        if (numberOfBullets == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * Method for creating bullets and setting them to move.
     * @param target agent that should be shoot at
     * @param tpf time per frame
     * @throws Exception when there is no more bullets left
     */
    public void shootAt(Agent target, float tpf) throws Exception{
        if (numberOfBullets == 0) {
            throw new Exception("No more bullets");
        }
        Bullet firedBullet = controlShootAt(target, tpf);
        Game.getInstance().addBullet(firedBullet);
        if (numberOfBullets != -1) {
            numberOfBullets--;
        }
    }
    
    protected abstract Bullet controlShootAt(Agent target, float tpf);
    
    /**
     * Method for creating bullets and setting them to move.
     * @param direction direction of bullets to go
     * @param tpf time per frame
     * @throws Exception when hasBullets()== false
     */
    public void shootAt(Vector3f direction, float tpf) throws Exception{
        if (numberOfBullets == 0) {
            throw new Exception("No more bullets");
        }
        Bullet firedBullet = controlShootAt(direction, tpf);
        //((Node) Game.getInstance().getRootNode()).attachChild(firedBullet.getSpatial());
        Game.getInstance().addBullet(firedBullet);
        if (numberOfBullets != -1) {
            numberOfBullets--;
        }
    }
    
    protected abstract Bullet controlShootAt(Vector3f direction, float tpf);

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

    public Spatial getSpatial() {
        return spatial;
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

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
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
    
}
