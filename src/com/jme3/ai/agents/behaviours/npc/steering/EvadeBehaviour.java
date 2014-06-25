//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Brent Owens: "Evasion is analogous to pursuit, except that flee is used to steer away 
 * from the predicted future position of the target character."
 * 
 * @author Jesús Martín Berlanga
 * @version 1.1
 */
public class EvadeBehaviour extends FleeBehaviour {

    
    /** @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent)    */
    public EvadeBehaviour(Agent agent, Agent target) {
        super(agent, target);
    }

     /** @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)   */
    public EvadeBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, target, spatial);
    }
   
    /** @see FleeBehaviour#calculateFullSteering()  */
    @Override
    protected Vector3f calculateFullSteering() 
    { 
        Vector3f projectedLocation = this.getTarget().getPredictedPosition();
        
        //Return flee steering force
        Vector3f desiredVelocity = projectedLocation.subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        return desiredVelocity.subtract(velocity).negate();  
    }

}
