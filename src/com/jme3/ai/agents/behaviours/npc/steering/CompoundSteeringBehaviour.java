//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 * A steer compound behaviour contains one or more steer behaviours.
 * This class should be used to avoid bugs when adding several
 * steer behaviours to SimpleMainBehaviour. <br> <br>
 * 
 * Fist add several behaviours to make an CompoundBehaviour and then
 * add this compoundBehaviour to an SimpleMainBehaviour. <br><br>
 * 
 * All the steer behaviours added to this class must extend from
 * AbstractSteeringBehaviour.
 * 
 * @author Jesús Martín Berlanga
 */
public class CompoundSteeringBehaviour extends AbstractSteeringBehaviour {

    private List<AbstractSteeringBehaviour> behaviours;
    
    protected List<AbstractSteeringBehaviour> getBehaviours() {
        return this.behaviours;
    }
    
    public void setSteerBehaviours(List<AbstractSteeringBehaviour> behaviours) {
        this.behaviours = behaviours;
    }
    
    /** @see  AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)  */
    public CompoundSteeringBehaviour(Agent agent) 
    { 
        super(agent);
        this.behaviours = new ArrayList<AbstractSteeringBehaviour>();
    }
    
    /** @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)  */
    public CompoundSteeringBehaviour(Agent agent, Spatial spatial) 
    { 
        super(agent, spatial);
        this.behaviours = new ArrayList<AbstractSteeringBehaviour>();
    }
    
    /**
     * Adds a behaviour to the compound behaviour
     * 
     * @param behaviour Behaviour that you want to add
     */
    public void addSteerBehaviour (AbstractSteeringBehaviour behaviour) {
        this.behaviours.add(behaviour);
    }


    /**
     * Calculates the composed steering force. The composed force is the sumatory
     * of the behaviours steering forces.
     * 
     * @return The composed steering force.
     */
    protected Vector3f calculateSteering() {
        
        Vector3f totalForce = new Vector3f();
        
        for(AbstractSteeringBehaviour steerBehaviour : behaviours)
        {
            totalForce = totalForce.add(this.calculatePartialForce(steerBehaviour));
        }
        
        return totalForce;
    }
    
    /**
     * Calculates the steering force of a single behaviour
     * @param behaviour The behaviour.
     * @return The steering force of that behaviour
     */
    protected Vector3f calculatePartialForce(AbstractSteeringBehaviour behaviour) {
        return behaviour.calculateSteering();
    }


    protected void controlRender(RenderManager rm, ViewPort vp) {  }

}
