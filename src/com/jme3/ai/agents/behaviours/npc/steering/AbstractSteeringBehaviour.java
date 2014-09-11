//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Base class for all steering behaviours. This behaviour contains some
 * attributes that are all common for steering behaviours. <br><br>
 *
 * You can change the braking factor (to a value lower than 1) so that this behaviour 
 * will slow down the agent velocity.
 * 
 * @author Tihomir Radosavljević
 * @author Jesús Martín Berlanga
 * @version 1.5
 */
public abstract class AbstractSteeringBehaviour extends Behaviour {
    
    /** @author Jesús Martín Berlanga */
    private float brakingFactor = 1;
    
    /** 
     *  @see IllegalBehaviour 
     *  @author Jesús Martín Berlanga 
     */
    public static final class IllegalBrakingFactor extends IllegalArgumentException { 
        private IllegalBrakingFactor(String msg) { super(msg); }
    }
    
    /** 
     *  Note that 0 means the maximum braking force and 1 No braking force 
     * 
     *  @throws IllegalBrakingFactor If the braking factor is not contained in the [0,1] interval
     * 
     *  @author Jesús Martín Berlanga 
     */
    protected final void setBrakingFactor(float brakingFactor) 
    { 
        if(brakingFactor  < 0 || brakingFactor > 1)
            throw new IllegalBrakingFactor("The braking factor value must be contained in the [0,1] interval. The current value is " + brakingFactor);
                    
        this.brakingFactor = brakingFactor; 
    }
    
    /** @author Jesús Martín Berlanga */
    public final float getBrakingFactor() { return this.brakingFactor; }
        
    /** @author Jesús Martín Berlanga */
    private float tpf;
    
    /** @author Jesús Martín Berlanga */
    protected float getTPF(){ return this.tpf; }
    
    /**
     * Manually update the tpf
     * @param tpf tpf
     * @author Jesús Martín Berlanga
     */
    protected void setTPF(float tpf) { this.tpf = tpf; }
    
    /**
     * Velocity of our agent.
     */
    protected Vector3f velocity;

    /** @see Behaviour#Behaviour(com.jme3.ai.agents.Agent) */
    public AbstractSteeringBehaviour(Agent agent) {
        super(agent);
        velocity = new Vector3f();
    }

    /** @see Behaviour#Behaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) */
    public AbstractSteeringBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        velocity = new Vector3f();
    }

    /**
     * Method for calculating steering vector.
     *
     * @return
     */
    protected abstract Vector3f calculateSteering();
    
    /**
     * Method for calculating new velocity of agent based on steering vector.
     * 
     * @see AbstractSteeringBehaviour#calculateSteering()
     * 
     * @return The new velocity for this agent based on steering vector
     * 
     * @author Jesús Martín Berlanga
     * @author Tihomir Radosavljević
     */
    protected Vector3f calculateNewVelocity() 
    {
        agent.setAcceleration(this.calculateSteering().mult(1 / agentTotalMass()));
        velocity = velocity.add(agent.getAcceleration());
        agent.setVelocity(velocity);
  
        if (velocity.length() > agent.getMaxMoveSpeed()) 
            velocity = velocity.normalize().mult(agent.getMaxMoveSpeed());
        
        return velocity;
    }

    /**
     * Method for rotating agent in direction of velocity of agent.
     *
     * @param tpf time per frame
     */
    protected void rotateAgent(float tpf) {
        Quaternion q = new Quaternion();
        q.lookAt(velocity, new Vector3f(0, 1, 0));
        agent.getLocalRotation().slerp(q, agent.getRotationSpeed() * tpf);
    }

    /**
     * Method for calculating agent total mass. It contains agent mass and mass
     * of weapon that agent is carrying. If there is inventory with mass, this
     * method should be overriden.
     *
     * @return total mass of agents
     */
    protected float agentTotalMass() {
        float mass = 0;
        mass += agent.getMass();
        if (agent.getWeapon() != null) {
            mass += agent.getWeapon().getMass();
        }
        return mass;
    }

    /**
     *
     * @return current velocity of agent.
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
     * Usual update pattern for steering behaviours. <br><br>
     * 
     * The final velocity is multiplied by the braking factor.
     *
     * @param tpf
     * 
     * @author Jesús Martín Berlanga
     * @author Tihomir Radosavljević
     */
    @Override
    protected void controlUpdate(float tpf) {
        this.tpf = tpf;
        
        Vector3f vel = calculateNewVelocity().mult(tpf).mult(this.brakingFactor);

        agent.setLocalTranslation(agent.getLocalTranslation().add(vel));
        rotateAgent(tpf);
    }
}
