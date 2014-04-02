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
 * PhysicalObject use:
 * @see Game#addGameObject(com.jme3.ai.agents.util.PhysicalObject)
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class PhysicalObject extends AbstractControl {

    /**
     * Mass of PhysicalObject.
     */
    protected float mass;
    /**
     * PhysicalObject acceleration speed.
     */
    protected Vector3f acceleration;
    /**
     * Current move speed of PhysicalObject.
     */
    protected float moveSpeed;
    /**
     * Maximum move speed of PhysicalObject
     */
    protected float maxMoveSpeed;
    /**
     * Maximum force that can be applied to this PhysicalObject.
     */
    protected float maxForce;
    /**
     * Current hitPoint status of PhysicalObject.
     */
    protected float hitPoint = 100f;
    /**
     * Maximum hitPoint that PhysicalObject can have.
     */
    protected float maxHitPoint = 100f;
    /**
     * Rotation speed of PhysicalObject.
     */
    protected float rotationSpeed;

    /**
     * Method for increasing agents hitPoint for fixed amount. If adding
     * hitPoint will cross the maximum hitPoint of agent, then agent's hitPoint
     * status will be set to maximum allowed hitPoint for that agent.
     *
     * @see Agent#maxHitPoint
     * @param potionHitPoint amount of hitPoint that should be added
     */
    public void increaseHitPoint(float potionHitPoint) {
        hitPoint += potionHitPoint;
        if (hitPoint > maxHitPoint) {
            hitPoint = maxHitPoint;
        }
    }

    /**
     * Method for decreasing agents hitPoint for fixed amount. If hitPoint drops
     * to zero or bellow, agent's hitPoint status will be set to zero and he
     * will be dead.
     *
     * @see Agent#alive
     * @param damage amount of hitPoint that should be removed
     */
    public void decreaseHitPoints(double damage) {
        hitPoint -= damage;
        if (hitPoint <= 0) {
            hitPoint = 0;
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

    public float getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(float hitPoint) {
        if (maxHitPoint < hitPoint) {
            this.hitPoint = maxHitPoint;
        }
        this.hitPoint = hitPoint;
    }

    public float getMaxHitPoint() {
        return maxHitPoint;
    }

    public void setMaxHitPoint(float maxHitPoint) {
        this.maxHitPoint = maxHitPoint;
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