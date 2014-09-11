//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. 
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.util;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.npc.steering.ObstacleAvoidanceBehaviour;
import com.jme3.ai.agents.util.control.Game;
import com.jme3.math.FastMath;
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
 * @author Jesús Martín Berlanga
 * @version 1.2.0
 */
public abstract class GameObject extends AbstractControl {

    /**
     * Container for the velocity of the game object.
     */
    protected Vector3f velocity;
    /**
     * Mass of GameObject.
     */
    protected float mass;
    /**
     * GameObject acceleration speed.
     */
    protected Vector3f acceleration;
    /**
     * Maximum move speed of GameObject
     */
    protected float maxMoveSpeed;
    /**
     * Maximum force that can be applied to this GameObject.
     */
    protected float maxForce;
    /**
     * Current hitPoint status of GameObject.
     */
    protected float hitPoint = 100f;
    /**
     * Maximum hitPoint that GameObject can have.
     */
    protected float maxHitPoint = 100f;
    /**
     * Rotation speed of GameObject.
     */
    protected float rotationSpeed;
    /**
     * Radius of GameObject. It is needed for object that will be added in list
     * of objects that agent should avoid durring game, like mines etc.
     *
     * @see ObstacleAvoidanceBehaviour
     */
    protected float radius = 0;

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

    /**
     * @return The predicted position for this 'frame', taking into account
     * current position and velocity.
     */
    public Vector3f getPredictedPosition() {
        Vector3f predictedPos = new Vector3f();
        if (velocity != null) {
            predictedPos = getLocalTranslation().add(velocity);
        }
        return predictedPos;
    }

    /**
     * @param gameObject Other game object
     * @return The offset relative to another game object
     */
    public Vector3f offset(GameObject gameObject) {
        return gameObject.getLocalTranslation().subtract(getLocalTranslation());
    }

    /**
     * @param positionVector
     * @return The offset relative to an position vector
     */
    public Vector3f offset(Vector3f positionVector) {
        return positionVector.subtract(getLocalTranslation());
    }

    /**
     * @return The agent forward direction
     */
    public Vector3f fordwardVector() {
        return getLocalRotation().mult(new Vector3f(0, 0, 1)).normalize();
    }

    /**
     * Calculates the forwardness in relation with another game object. That is
     * how "forward" is the direction to the quarry (1 means dead ahead, 0 is
     * directly to the side, -1 is straight back)
     *
     * @param gameObject Other game object
     * @return The forwardness in relation with another agent
     */
    public float forwardness(GameObject gameObject) {
        Vector3f agentLooks = fordwardVector();
        float radiansAngleBetwen = agentLooks.angleBetween(offset(gameObject).normalize());
        return FastMath.cos(radiansAngleBetwen);
    }

    /**
     * @param positionVector Offset vector.
     * @return The forwardness in relation with a position vector
     */
    public float forwardness(Vector3f offsetVector) {
        Vector3f agentLooks = getLocalRotation().mult(new Vector3f(0, 0, 1)).normalize();
        float radiansAngleBetwen = agentLooks.angleBetween(offsetVector.normalize());
        return FastMath.cos(radiansAngleBetwen);
    }

    /**
     * @param gameObject Other agent
     * @return Distance relative to another game object
     */
    public float distanceRelativeToGameObject(GameObject gameObject) {
        return offset(gameObject).length();
    }

    /**
     * @param gameObject Other agent
     * @return Distance from a position
     */
    public float distanceSquaredRelativeToGameObject(GameObject gameObject) {
        return offset(gameObject).lengthSquared();
    }

    /**
     * @param pos Position
     * @return Distance from a position
     */
    public float distanceFromPosition(Vector3f pos) {
        return offset(pos).length();
    }

    /**
     * @param pos Position
     * @return Distance squared Distance from a position
     */
    public float distanceSquaredFromPosition(Vector3f pos) {
        return offset(pos).lengthSquared();
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
        return velocity.length();
    }

    public void setMoveSpeed(float moveSpeed) {
        if (maxMoveSpeed < moveSpeed) {
            this.maxMoveSpeed = moveSpeed;
        }
        velocity.normalizeLocal().multLocal(moveSpeed);
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

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }

    public void setRadius(float radius) {
        validateRadius(radius);
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    /**
     * @see IllegalArgumentException
     * @author Jesús Martín Berlanga
     */
    public static class NegativeRadiusException extends IllegalArgumentException {

        private NegativeRadiusException(String msg) {
            super(msg);
        }
    }

    protected void validateRadius(float radius) {
        if (radius < 0) {
            throw new NegativeRadiusException("A GameObject can't have a negative radius. You tried to construct the agent with a " + radius + " radius.");
        }
    }
}
