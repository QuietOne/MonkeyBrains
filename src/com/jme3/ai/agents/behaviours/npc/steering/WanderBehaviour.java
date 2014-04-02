package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.Random;

/**
 * Wander behaviour is steering behaviour where agent moves randomly on terrain.
 * This is done by same calculation as seek behaviour, but difference is that
 * target for this behaviour are random positions that changes durring time.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class WanderBehaviour extends AbstractSteeringBehaviour {

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
    /**
     * Area in which agent will wander.
     */
    protected Vector3f[] area;

    /**
     * Constructor for wander behaviour.
     *
     * @param agent to whom behaviour belongs
     */
    public WanderBehaviour(Agent agent) {
        super(agent);
        targetPosition = new Vector3f();
        velocity = new Vector3f();
        timeInterval = 2f;
        time = timeInterval;
        area = new Vector3f[2];
    }

    /**
     * Constructor for wander behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param spatial active spatial during excecution of behaviour
     */
    public WanderBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        targetPosition = new Vector3f();
        velocity = new Vector3f();
        timeInterval = 2f;
        time = timeInterval;
    }

    @Override
    protected void controlUpdate(float tpf) {
        changeTargetPosition(tpf);
        super.controlUpdate(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("You should override it youself");
    }

    /**
     * Calculate steering vector.
     *
     * @return steering vector
     */
    public Vector3f calculateSteering() {
        Vector3f desiredVelocity = targetPosition.subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        return desiredVelocity.subtract(velocity);
    }

    /**
     * Calculate new velocity for agent based on calculated steering behaviour.
     *
     * @return velocity vector
     */
    public Vector3f calculateNewVelocity() {
        Vector3f steering = calculateSteering();
        if (steering.length() > agent.getMaxForce()) {
            steering = steering.normalize().mult(agent.getMaxForce());
        }
        agent.setAcceleration(steering.mult(1 / agentTotalMass()));
        velocity = velocity.add(agent.getAcceleration());
        if (velocity.length() > agent.getMoveSpeed()) {
            velocity = velocity.normalize().mult(agent.getMoveSpeed());
        }
        return velocity;
    }

    /**
     * Metod for changing target position.
     *
     * @param tpf time per frame
     */
    public void changeTargetPosition(float tpf) {
        time -= tpf;
        if (time <= 0) {
            Random random = new Random();
            float x, z;
            int distance = (int) FastMath.abs(area[1].x - area[0].x);
            x = random.nextInt(distance / 2);
            if (random.nextBoolean()) {
                x *= -1;
            }
            distance = (int) FastMath.abs(area[1].z - area[0].z);
            z = random.nextInt(distance / 2);
            if (random.nextBoolean()) {
                z *= -1;
            }
            targetPosition = new Vector3f(x, agent.getLocalTranslation().getY(), z);
            time = timeInterval;
        }
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
     * Setting area for wander.
     * @param from
     * @param to 
     */
    public void setArea(Vector3f from, Vector3f to){
        area[0] = from;
        area[1] = to;
    }
    
}
