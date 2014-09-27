//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. 
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.npc.steering.SteeringExceptions.NegativeScalarMultiplierException;

import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * With this class it will be possible to increase or decrease the steering
 * behaviour force.
 * <br> <br>
 *
 * It can be changed the length of this vector itself, multiplying it by a
 * scalar or modifying the force on a especific axis (x, y, z) <br> <br>
 *
 * This class also allows you force the steering to stay within a plane.
 * <br><br>
 *
 * You need to call setupStrengthControl( ... ), otherwhise this class will work
 * the same as AstractSteeringBehaviour.
 *
 * @see SteerStrengthType
 * @see AbstractSteeringBehaviour
 *
 * @author Jesús Martín Berlanga
 * @version 2.1.0
 */
public abstract class AbstractStrengthSteeringBehaviour extends AbstractSteeringBehaviour {

    /**
     * Defines how the "strength" will be applied to a steer force.
     */
    private static enum SteerStrengthType {

        NO_STRENGTH,
        SCALAR,
        AXIS,
        PLANE
    }
    /**
     * Type of steer force that is applied to steering behaviour.
     */
    private SteerStrengthType type = SteerStrengthType.NO_STRENGTH;
    /**
     * Used if the steer type is scalar.
     */
    private float scalar;
    /**
     * Used if the steer type is axis.
     */
    private float x, y, z;
    /**
     * Used if the steer type is plane.
     */
    private Plane plane;

    /**
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public AbstractStrengthSteeringBehaviour(Agent agent) {
        super(agent);
    }

    /**
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public AbstractStrengthSteeringBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
    }

    /**
     * If you call this function you will be able to increase or decrease the
     * steering behaviour force multiplying it by a scalar.
     *
     * @param scalar Escalar that will multiply the full steer force.
     *
     * @throws NegativeScalarMultiplierException If scalar is lower than 0
     */
    public void setupStrengthControl(float scalar) {
        this.validateScalar(scalar);
        this.scalar = scalar;
        this.type = SteerStrengthType.SCALAR;
    }

    /**
     * If you call this function you will be able to modify the full steering
     * force on a especific axis (x, y, z)
     *
     * @param x X axis multiplier
     * @param y Y axis multiplier
     * @param z Z axis multiplier
     *
     * @throws NegativeScalarMultiplierException If any axis multiplier is lower
     * than 0
     */
    public void setupStrengthControl(float x, float y, float z) {
        this.validateScalar(x);
        this.validateScalar(y);
        this.validateScalar(z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = SteerStrengthType.AXIS;
    }

    /**
     * If you call this function you will be able to modify the full steering
     * force on a especific axis (x, y, z)
     *
     * @param x X axis multiplier
     * @param y Y axis multiplier
     * @param z Z axis multiplier
     *
     * @throws NegativeScalarMultiplierException If any axis multiplier is lower
     * than 0
     */
    public void setupStrengthControl(Vector3f vector) {
        validateScalar(vector.getX());
        validateScalar(vector.getY());
        validateScalar(vector.getZ());
        x = vector.getX();
        y = vector.getY();
        z = vector.getZ();
        type = SteerStrengthType.AXIS;
    }

    /**
     * Forces the steer to stay inside a plane.
     *
     * @param Plane plane where the steer will be
     */
    public void setupStrengthControl(Plane plane) {
        this.scalar = 1.0f;
        this.plane = plane;
        this.type = SteerStrengthType.PLANE;
    }

    /**
     * @see AbstractStrengthSteeringBehaviour#setupStrengthControl(float)
     * @see
     * AbstractStrengthSteeringBehaviour#setupStrengthControl(com.jme3.math.Plane)
     */
    public void setupStrengthControl(Plane plane, float scalar) {
        this.validateScalar(scalar);
        this.scalar = scalar;
        this.plane = plane;
        this.type = SteerStrengthType.PLANE;
    }

    private void validateScalar(float scalar) {
        if (scalar < 0) {
            throw new NegativeScalarMultiplierException("All the scalar multipliers must be positives.");
        }
    }

    /**
     * If this function is called, this class work as AbstractSteeringBehaviour.
     *
     * @see AbstractSteeringBehaviour
     */
    public void turnOffStrengthControl() {
        this.type = SteerStrengthType.NO_STRENGTH;
    }

    /**
     * Calculates the steering force with the especified strength. <br><br>
     *
     * If the strength was not setted up it the return calculateSteering(), the
     * unmodified force.
     *
     * @return The steering force with the especified strength.
     */
    @Override
    protected Vector3f calculateSteering() {

        Vector3f strengthSteeringForce = calculateRawSteering();

        switch (this.type) {
            case SCALAR:
                strengthSteeringForce = strengthSteeringForce.mult(this.scalar);
                break;

            case AXIS:
                strengthSteeringForce.setX(strengthSteeringForce.getX() * this.x);
                strengthSteeringForce.setY(strengthSteeringForce.getY() * this.y);
                strengthSteeringForce.setZ(strengthSteeringForce.getZ() * this.z);
                break;

            case PLANE:
                strengthSteeringForce = this.plane.getClosestPoint(strengthSteeringForce).mult(this.scalar);
                break;

            default:
        }

        return strengthSteeringForce;
    }

    /**
     * If a behaviour class extend from CompoundSteeringBehaviour instead of
     * AbstractSteeringBehaviour, it must implement this method instead of
     * calculateSteering().
     *
     * @see AbstractSteeringBehaviour#calculateSteering()
     * @return
     */
    protected abstract Vector3f calculateRawSteering();
}