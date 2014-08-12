//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * With this class it will be able to increase or decrease the steering behaviour force.
 * <br> <br>
 * 
 * It can be changed the length of this vector itself, multiplying it by a scalar
 * or modifying the force on a especific axis (x, y, z) <br> <br>
 * 
 * This class also allows you force the steering to stay within a plane. <br><br>
 *
 * You need to call setupStrengthControl( ... ), otherwhise this class will work
 * the same as AstractSteeringBehaviour.
 * 
 * @see SteerStrengthType
 * @see AbstractSteeringBehaviour
 * 
 * @author Jesús Martín Berlanga
 * @version 2.0
 */
public abstract class AbstractStrengthSteeringBehaviour extends AbstractSteeringBehaviour {


    // Defines how the "strength" will be applied to a steer force.
    private static enum SteerStrengthType 
    {
        NO_STRENGTH,
        ESCALAR,
        AXIS,
        PLANE
    }
    
    private SteerStrengthType type = SteerStrengthType.NO_STRENGTH;
    private Float escalar, x, y, z;
    private Plane plane;

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
        this.type = SteerStrengthType.ESCALAR;
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
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = SteerStrengthType.AXIS;
    }
    
    /**
     * Forces the steer to stay inside a plane.
     * 
     * @param normal Vector normal to the plane
     */
    public void setupStrengthControl(Vector3f normal) {
        this.escalar = 1.0f;
        this.plane = new Plane(normal, 0);
        this.type = SteerStrengthType.PLANE;
    }
    
    /**
     * @see AbstractStrengthSteeringBehaviour#setupStrengthControl(com.jme3.math.Vector3f) 
     * @param Plane plane where the steer will be
     */
    public void setupStrengthControl(Plane plane) {
       this.escalar = 1.0f;
        this.plane = plane;
        this.type = SteerStrengthType.PLANE;
    }
    
    /**
     * The force will also be multiplied by an escalar.
     * 
     * @see AbstractStrengthSteeringBehaviour#setupStrengthControl(com.jme3.math.Vector3f) 
     * @param escalar Escalar that will multiply the full steer force.
     */
    public void setupStrengthControl(Vector3f normal, float escalar) {
        this.escalar = escalar;
        this.plane = new Plane(normal, 0);
        this.type = SteerStrengthType.PLANE;
    }
    
    /**
     * @see AbstractStrengthSteeringBehaviour#setupStrengthControl(com.jme3.math.Vector3f, float) 
     * @see AbstractStrengthSteeringBehaviour#setupStrengthControl(com.jme3.math.Vector3f) 
     */
    public void setupStrengthControl(Plane plane, float escalar) {
        this.escalar = escalar;
        this.plane = plane;
        this.type = SteerStrengthType.PLANE;
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
        
        switch(this.type)
        {
            case ESCALAR:
                strengthSteeringForce = strengthSteeringForce.mult(this.escalar);
                break;
                
            case AXIS:
                strengthSteeringForce.setX( strengthSteeringForce.getX() * this.x );
                strengthSteeringForce.setY( strengthSteeringForce.getY() * this.y );
                strengthSteeringForce.setZ( strengthSteeringForce.getZ() * this.z );
                break;
                
            case PLANE:
                strengthSteeringForce = this.plane.getClosestPoint(strengthSteeringForce).mult(this.escalar);
                break;
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