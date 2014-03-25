package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Flee is simply the inverse of seek and acts to steer the agent so that its
 * velocity is radially aligned away from the target. The desired velocity
 * points in the opposite direction. Basically it's movement behaviour. For your
 * use of seek behaviour you only need to override controlUpdate() and
 * controlRender() and use appropriate listeners. For now it hasn't been done,
 * because this is in demo version.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class FleeBehaviour extends Behaviour {

    /**
     * Agent from whom we flee.
     */
    private Agent target;
    /**
     * Velocity of our agent.
     */
    private Vector3f velocity;

    /**
     * Constructor for flee behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent from whom we flee
     */
    public FleeBehaviour(Agent agent, Agent target) {
        super(agent);
        this.target = target;
        velocity = new Vector3f();
    }

    /**
     * Constructor for flee behaviour.
     *
     * @param agent to whom behaviour belongs
     * @param target agent from whom we flee
     * @param spatial active spatial during excecution of behaviour
     */
    public FleeBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, spatial);
        this.target = target;
        velocity = new Vector3f();
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f vel = calculateNewVelocity().mult(tpf);
        agent.setLocalTranslation(agent.getLocalTranslation().add(vel));
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
        Vector3f desiredVelocity = target.getLocalTranslation().subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        return desiredVelocity.subtract(velocity).negate();
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
        agent.setAcceleration(steering.mult(1 / agent.getMass()));
        velocity = velocity.add(agent.getAcceleration());
        if (velocity.length() > agent.getMoveSpeed()) {
            velocity = velocity.normalize().mult(agent.getMoveSpeed());
        }
        return velocity;
    }

    /**
     * Get current velocity of agent.
     *
     * @return velocity vector
     */
    public Vector3f getVelocity() {
        return velocity;
    }

    /**
     * Setting current velocity of agent.
     *
     * @param velocity
     */
    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    /**
     * Get agent from whom we flee.
     *
     * @return agent
     */
    public Agent getTarget() {
        return target;
    }

    /**
     * Setting agent from whom we flee.
     *
     * @param target
     */
    public void setTarget(Agent target) {
        this.target = target;
    }
}
