package com.jme3.ai.agents.util;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.control.AbstractControl;

/**
 * Base class for game objects that are interacting in game, and in general can
 * move and can be destroyed. Not to be used for terrain except for things that
 * can be destroyed. For automaticaly updating them, add them to to Game with
 * addAgent().
 *
 * @see Game#addAgent(com.jme3.ai.agents.Agent)
 * @see Game#addAgent(com.jme3.ai.agents.Agent, com.jme3.math.Vector3f)
 * @see Game#addAgent(com.jme3.ai.agents.Agent, float, float, float) For other
 * GameObject use:
 * @see Game#addGameObject(com.jme3.ai.agents.util.GameObject)
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class GameObject extends AbstractControl {

    /**
     * Mass of GameObject.
     */
    protected float mass;
    /**
     * GameObject acceleration speed.
     */
    protected Vector3f acceleration;
    /**
     * Current move speed of GameObject.
     */
    protected float moveSpeed;
    /**
     * Maximum move speed of GameObject
     */
    protected float maxMoveSpeed;
    /**
     * Maximum force that can be applied to this GameObject.
     */
    protected float maxForce;
    /**
     * Current health status of GameObject.
     */
    protected float health;
    /**
     * Maximum health that GameObject can have.
     */
    protected float maxHealth = 100f;
    /**
     * Rotation speed of GameObject.
     */
    protected float rotationSpeed;

    /**
     * Method for increasing agents health for fixed amount. If adding health
     * will cross the maximum health of agent, then agent's health status will
     * be set to maximum allowed health for that agent.
     *
     * @see Agent#maxHealth
     * @param potionHealth amount of health that should be added
     */
    public void increaseHealth(float potionHealth) {
        health += potionHealth;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * Method for decreasing agents health for fixed amount. If health drops to
     * zero or bellow, agent's health status will be set to zero and he will be
     * dead.
     *
     * @see Agent#alive
     * @param damage amount of health that should be removed
     */
    public void decreaseHealth(double damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            enabled = false;
        }
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Vector3f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        if (maxMoveSpeed < moveSpeed) {
            this.moveSpeed = maxMoveSpeed;
        }
        this.moveSpeed = moveSpeed;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        if (maxHealth < health) {
            this.health = maxHealth;
        }
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Quaternion getLocalRotation() {
        return spatial.getLocalRotation();
    }

    public void setLocalRotation(Quaternion rotation) {
        spatial.setLocalRotation(rotation);
    }

    /**
     *
     * @return local translation of agent
     */
    public Vector3f getLocalTranslation() {
        return spatial.getLocalTranslation();
    }

    /**
     *
     * @param position local translation of agent
     */
    public void setLocalTranslation(Vector3f position) {
        this.spatial.setLocalTranslation(position);
    }

    /**
     * Setting local translation of agent
     *
     * @param x x translation
     * @param y y translation
     * @param z z translation
     */
    public void setLocalTranslation(float x, float y, float z) {
        this.spatial.setLocalTranslation(x, y, z);
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getMaxMoveSpeed() {
        return maxMoveSpeed;
    }

    public void setMaxMoveSpeed(float maxMoveSpeed) {
        this.maxMoveSpeed = maxMoveSpeed;
    }
}