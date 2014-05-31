//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * With this class it will be able to increase or decrease the steering behaviour force.
 * <br> <br>
 * 
 * It can be changed the length of this vector itself, multiplying it by a scalar
 * or modifying the force on a especific axis (x, y, z) <br> <br>
 * 
 * This class is specially useful when we only need the behaviour to work in the
 * plane (g.e. set Z to 0) or We have a compoud steer behaviour. <br> <br>
 * 
 * You need to call setupStrengthControl( ... ), otherwhise this class will work
 * the same as AstractSteeringBehaviour.
 * 
 * @see AbstractSteeringBehaviour
 * 
 * @author Jesús Martín Berlanga
 */
public abstract class AbstractStrengthSteeringBehaviour extends AbstractSteeringBehaviour {

    private boolean strengthSettedUp = false;
    private Float escalar, x, y, z;

    /** @see  AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)  */
    public AbstractStrengthSteeringBehaviour(Agent agent) { super(agent); }
    
    /** @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)  */
    public AbstractStrengthSteeringBehaviour(Agent agent, Spatial spatial) { super(agent, spatial); }
    
    /**
     * If you call this function you will be able to increase or decrease the steering
     * behaviour force multiplying it by a scalar.
     * 
     * @param escalar Escalar that will multiply the full steer force.
     */
    public void setupStrengthControl(float escalar) {
        this.escalar = escalar;
        this.x = null;
        this.y = null;
        this.z = null;
        this.strengthSettedUp = true;
    }
    
     /**
     * If you call this function you will be able to modify the full steering force
     * on a especific axis (x, y, z) 
     * 
     * @param x X axis multiplier
     * @param y Y axis multiplier
     * @param z Z axis multiplier
     */
    public void setupStrengthControl(float x, float y, float z) {
        this.escalar = null;
        this.x = x;
        this.y = y;
        this.z = z;
        this.strengthSettedUp = true;
    }
    
    /**
     * If this function is called, this class work as AbstractSteeringBehaviour.
     * 
     * @see AbstractSteeringBehaviour
     */
    public void turnOffStrengthControl() { 
        this.escalar = null;
        this.x = null;
        this.y = null;
        this.z = null;
        this.strengthSettedUp = false; 
    }
  
    /**
     * Calculates the steering force with the especified strength.
     * 
     * If the strength was not setted up it the return calculateSteering(),
     * the unmodified force.
     * 
     * @return The steering force with the especified strength.
     */
    @Override
    protected Vector3f calculateSteering() {
        
        Vector3f strengthSteeringForce = this.calculateFullSteering();
        
        if(strengthSettedUp)
        {
            if(this.escalar != null)
                strengthSteeringForce = strengthSteeringForce.mult(this.escalar);
            else
            {
                strengthSteeringForce.setX( strengthSteeringForce.getX() * this.x );
                strengthSteeringForce.setY( strengthSteeringForce.getX() * this.y );
                strengthSteeringForce.setZ( strengthSteeringForce.getX() * this.z );
            }
        }
        
        return strengthSteeringForce;
    }
    
    /**
     * If a bheaviour class extend from CompoundSteeringBehaviour instead
     * of AbstractSteeringBehaviout, It must implement this method instead of
     * calculateSteering().
     * 
     * @see AbstractSteeringBehaviour#calculateSteering() 
     */ 
    abstract Vector3f calculateFullSteering();
}
