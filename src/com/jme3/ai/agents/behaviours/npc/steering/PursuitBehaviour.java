//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Brent Owens: "Pursuit is similar to seek except that the quarry (target) is another moving
 * character. Effective pursuit requires a prediction of the target’s future position." 
 *
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class PursuitBehaviour extends SeekBehaviour {
    
    /** @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent)  */
    public PursuitBehaviour(Agent agent, Agent target) {
        super(agent, target);
    }
   
    /** @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)  */
    public PursuitBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, target, spatial);
    }

    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering()  */
    @Override
    protected Vector3f calculateFullSteering() 
    {   
        // calculate speed difference to see how far ahead we need to leed
        Vector3f projectedLocation = this.getTarget().getPredictedPosition();
        
        //Seek behaviour
        Vector3f desierdVel = projectedLocation.subtract(this.agent.getLocalTranslation()).normalize().mult(this.agent.getMoveSpeed());
        
        Vector3f aVelocity = this.agent.getVelocity();
        
        if(aVelocity == null)
            aVelocity = new Vector3f();
        
        return desierdVel.subtract(aVelocity);
    }
    
}
