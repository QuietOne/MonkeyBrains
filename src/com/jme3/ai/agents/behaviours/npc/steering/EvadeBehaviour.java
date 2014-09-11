//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviour;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Brent Owens: "Evasion is analogous to pursuit, except that flee is used to steer away 
 * from the predicted future position of the target character."
 * 
 * @author Jesús Martín Berlanga
 * @version 1.2
 */
public class EvadeBehaviour extends FleeBehaviour {

    /** 
     * @throws EvadeWithoutTarget If target is null
     * @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent)    
     */
    public EvadeBehaviour(Agent agent, Agent target) {
        super(agent, target);
        this.validateTarget(target);
    }

     /**
      * @throws EvadeWithoutTarget If target is null
      * @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)   
      */
    public EvadeBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, target, spatial);
        this.validateTarget(target);
    }
   
    /** @see IllegalBehaviour */
    public static class EvadeWithoutTarget extends IllegalBehaviour {
        private EvadeWithoutTarget(String msg) { super(msg); }
    }
    
    private void validateTarget(Agent target) {
        if(target == null) throw new EvadeWithoutTarget("The target can not be null.");
    }
    
    /** @see FleeBehaviour#calculateFullSteering()  */
    @Override
    protected Vector3f calculateFullSteering() 
    { 
        Vector3f projectedLocation = this.getTarget().getPredictedPosition();
        
        //Return flee steering force
        Vector3f desiredVelocity = projectedLocation.subtract(agent.getLocalTranslation());
        return desiredVelocity.subtract(velocity).negate();  
    }

}
