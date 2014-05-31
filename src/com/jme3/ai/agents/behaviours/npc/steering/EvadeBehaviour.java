//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Brent Owens: "Evasion is analogous to pursuit, except that flee is used to steer away 
 * from the predicted future position of the target character."
 * 
 * @author Jesús Martín Berlanga
 */
public class EvadeBehaviour extends FleeBehaviour {

    
    /** @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent)    */
    public EvadeBehaviour(Agent agent, Agent target) {
        super(agent, target);
    }

     /** @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)   */
    public EvadeBehaviour(Agent agent, Spatial spatial, Agent target) {
        super(agent, target, spatial);
    }
   
    /** @see FleeBehaviour#calculateSteering(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) */
    @Override
    protected Vector3f calculateSteering() {
        
        //Calculate future position
        Vector3f projectedLocation = agent.calculateProjectedLocation(this.getTarget());
        Agent projectedAgent = new Agent("Projected Target Agent", 
                new Node("Projected Target Node").move(projectedLocation));
        
        this.setTarget(projectedAgent);
        
        //Return flee steering force
        return super.calculateSteering();   
    }

}
