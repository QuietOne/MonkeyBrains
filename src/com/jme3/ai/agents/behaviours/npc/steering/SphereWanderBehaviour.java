//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.util.Random;

/**
 * "Wander is a type of random steering. This idea can be implemented several
 * ways, but one that has produced good results is to constrain the steering
 * force to the surface of a sphere located slightly ahead of the character. To
 * produce the steering force for the next frame: a random displacement is added
 * to the previous value, and the sum is constrained again to the sphere's
 * surface. The sphere's radius determines the maximum wandering strength and
 * the magnitude of the random displacement determines the wander 'rate'."
 * <br><br>
 *
 * The steer force is contained in the XY plane
 *
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class SphereWanderBehaviour extends AbstractStrengthSteeringBehaviour {

    private static final float OFFSET_DISTANCE = 0.01f;
    private static final float RANDOM_OFFSET = 0.01f;
    private static final float SIDE_REFERENCE_OFFSET = 0.0001f;
    private float sphereRadius = 0.75f;
    /**
     * Position of target
     */
    protected Vector3f targetPosition;
    /**
     * Time interval durring which target position doesn't change.
     */
    protected float timeInterval;
    /**
     * Current time.
     */
    protected float time;
    private float randomFactor;
    private Vector2f randomDirection;
    private float maxRandom;
    private float rotationFactor;
    private BoundingSphere wanderSphere;

    /**
     * Constructor for wander behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param timeInterval Sets the time interval for changing target position.
     * @param randomFactor Defines the maximum random value
     * @param rotationFactor Defines the maximum random variaton for each
     * iteration.
     *
     * @throws SphereWanderWithoutTimeIntervalException If timeInterval is lower
     * or equals to 0
     * @throws SphereWanderInvalidRandomFactorException If randomFactor is not
     * contained in the [0,1] interval
     * @throws SphereWanderInvalidRotationFactorException If rotationFactor is
     * not contained in the [0,1] interval
     */
    public SphereWanderBehaviour(Agent agent, float timeInterval, float randomFactor, float rotationFactor) {
        super(agent);
        this.construct(timeInterval, randomFactor, rotationFactor);
    }

    /**
     * @see
     * SphereWanderBehaviour#SphereWanderBehaviour(com.jme3.ai.agents.Agent,
     * float, float, float)
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public SphereWanderBehaviour(Agent agent, float timeInterval, float randomFactor, float rotationFactor, Spatial spatial) {
        super(agent, spatial);
        this.construct(timeInterval, randomFactor, rotationFactor);
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderInvalidRandomFactorException extends IllegalBehaviourException {

        private SphereWanderInvalidRandomFactorException(String msg) {
            super(msg);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderInvalidRotationFactorException extends IllegalBehaviourException {

        private SphereWanderInvalidRotationFactorException(String msg) {
            super(msg);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderWithoutTimeIntervalException extends IllegalBehaviourException {

        private SphereWanderWithoutTimeIntervalException(String msg) {
            super(msg);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class SphereWanderWithoutRadiusException extends IllegalBehaviourException {

        private SphereWanderWithoutRadiusException(String msg) {
            super(msg);
        }
    }

    private void construct(float timeInterval, float randomFactor, float rotationFactor) {
        if (timeInterval <= 0) {
            throw new SphereWanderWithoutTimeIntervalException("The time interval must be possitive. The current value is: " + timeInterval);
        } else if (randomFactor < 0 || randomFactor > 1) {
            throw new SphereWanderInvalidRandomFactorException("The random factor value must be contained in the [0,1] interval. The current value is " + randomFactor);
        } else if (rotationFactor < 0 || rotationFactor > 1) {
            throw new SphereWanderInvalidRotationFactorException("The rotation factor value must be contained in the [0,1] interval. The current value is " + rotationFactor);
        }

        this.timeInterval = timeInterval;
        this.time = this.timeInterval;
        this.randomFactor = randomFactor;

        this.wanderSphere = new BoundingSphere(this.sphereRadius, Vector3f.ZERO);

        targetPosition = this.wanderSphere.getCenter();

        this.randomDirection = new Vector2f();
        this.maxRandom = this.sphereRadius - SphereWanderBehaviour.RANDOM_OFFSET;
        this.rotationFactor = rotationFactor;
        this.maxRandom *= this.rotationFactor;
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     */
    @Override
    protected Vector3f calculateRawSteering() {
        changeTargetPosition(this.getTPF());
        return this.agent.offset(this.targetPosition).mult((0.5f / this.sphereRadius) * this.agent.getMoveSpeed());
    }

    /**
     * Metod for changing target position.
     *
     * @param tpf time per frame
     */
    protected void changeTargetPosition(float tpf) {
        time -= tpf;
        Vector3f fordward;

        if (this.agent.getVelocity() != null) {
            fordward = this.agent.getVelocity().normalize();
        } else {
            fordward = this.agent.fordwardVector();
        }

        if (fordward.equals(Vector3f.UNIT_Y)) {
            fordward = fordward.add(new Vector3f(0, 0, SphereWanderBehaviour.SIDE_REFERENCE_OFFSET));
        }

        //Update sphere position  
        this.wanderSphere.setCenter(this.agent.getLocalTranslation().add(fordward.mult(SphereWanderBehaviour.OFFSET_DISTANCE + this.agent.getRadius() + this.sphereRadius)));

        if (time <= 0) {
            this.calculateNewRandomDir();
            time = timeInterval;
        }

        Vector3f sideVector = fordward.cross(Vector3f.UNIT_Y).normalize();
        Vector3f rayDir = (this.agent.offset(wanderSphere.getCenter())).add(sideVector.mult(this.randomDirection.x));//.add(Vector3f.UNIT_Y.mult(this.randomDirection.y));       

        Ray ray = new Ray(this.agent.getLocalTranslation(), rayDir);
        CollisionResults results = new CollisionResults();
        this.wanderSphere.collideWith(ray, results);

        CollisionResult collisionResult = results.getCollision(1); //The collision with the second hemisphere
        this.targetPosition = collisionResult.getContactPoint();
    }

    protected void calculateNewRandomDir() {
        Random rand = new Random();

        float extraRandomSide = (rand.nextFloat() / 2) * this.sphereRadius * this.rotationFactor * this.randomFactor;
        //float extraRandomZ = (rand.nextFloat() / 2) * this.sphereRadius * this.rotationFactor * this.randomFactor ;

        if (rand.nextBoolean()) {
            extraRandomSide *= -1;
        }
        //if(rand.nextBoolean()) extraRandomZ *= -1;

        float exceededSideOffset;
        //float exceededZOffset;

        float predictecRandomSide = this.randomDirection.x + extraRandomSide;

        if (predictecRandomSide > this.maxRandom) {
            exceededSideOffset = this.maxRandom - predictecRandomSide;
        } else if (predictecRandomSide < -this.maxRandom) {
            exceededSideOffset = -predictecRandomSide + this.maxRandom;
        } else {
            exceededSideOffset = 0;
        }

        /*
         float predictedRandomUp = this.randomDirection.y + extraRandomZ;
        
         if(predictedRandomUp > this.maxRandom)
         exceededZOffset = this.maxRandom - predictedRandomUp;
         else if(predictedRandomUp < -this.maxRandom)
         exceededZOffset = -predictedRandomUp + this.maxRandom;
         else
         exceededZOffset = 0;
         */

        this.randomDirection = this.randomDirection.add(new Vector2f(
                extraRandomSide + exceededSideOffset,
                0 //extraRandomZ + exceededZOffset
                ));
    }

    /**
     * Get time interval for changing target position.
     *
     * @return
     */
    public float getTimeInterval() {
        return timeInterval;
    }

    /**
     * Setting time interval for changing target position.
     *
     * @param timeInterval
     */
    public void setTimeInterval(float timeInterval) {
        this.timeInterval = timeInterval;
    }

    /**
     * The sphere radius is 0.75 by default. Note that low radius values can
     * cause unexpected errors.
     *
     * @throws SphereWanderWithoutRadiusException If sphereRadius is lower or
     * equals to 0
     */
    public void setSphereRadius(float sphereRadius) {
        if (sphereRadius <= 0) {
            throw new SphereWanderWithoutRadiusException("The sphere radius must be possitive. Current value is: " + sphereRadius);
        }
        this.sphereRadius = sphereRadius;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
