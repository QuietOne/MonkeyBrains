//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Arrival behavior is identical to seek while the character is far from its target. 
 * But instead of moving through the target at full speed, this behavior causes the 
 * character to slow down as it approaches the target, eventually slowing to a stop 
 * coincident with the target
 *
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class ArriveBehaviour extends SeekBehaviour {

    private float slowingDistance;
    private SlowBehaviour slow;
    private float errorDistance = 0.01f;
    
    public void setErrorDistance(float errorDistance){ this.errorDistance = errorDistance; }
    public SlowBehaviour getSlow() { return this.slow; }
    
    /** 
     * The slowingDistance is (0.1 * distance betwen agents) by default. <br> <br>
     * The slow proportion is 0.5 by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target) { 
        super(agent, target);
        this.slowingDistance = agent.distanceRelativeToAgent(target) * 0.135f;
        this.slow = new SlowBehaviour(agent);
    }
    
    /**
     * The slowingDistance is (0.1 * distance betwen agents) by default. <br> <br>
     * The slow proportion is 0.5 by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Agent target, Spatial spatial) {  
        super(agent, target, spatial); 
        this.slowingDistance = agent.distanceRelativeToAgent(target) * 0.135f;
        this.slow = new SlowBehaviour(agent);
    }
    
    /**
     * @param slowingDistance The distance when this agent will start slowing down
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target, float slowingDistance, float slowPercentaje) {
        super(agent, target);
        this.slowingDistance = slowingDistance;
        this.slow = new SlowBehaviour(agent, slowPercentaje);
    }
    
    /**
     * @param slowingDistance The distance when this agent will start slowing down
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Agent target, Spatial spatial, float slowingDistance, float slowPercentaje) {
        super(agent, target, spatial);
        this.slowingDistance = slowingDistance;
        this.slow = new SlowBehaviour(agent, slowPercentaje);
    }
    
    /**
     * Calculate steering vector.
     *
     * @return steering vector
     * 
     * @see AbstractStrengthSteeringBehaviour#calculateFullSteering() 
     * @see SeekBehaviour#calculateFullSteering() 
     * 
     * @author Jesús Martín Berlanga
     */
    @Override
    protected Vector3f calculateFullSteering() 
    {
      Vector3f steer = new Vector3f();
      float distanceToTarget = this.agent.distanceRelativeToAgent(this.getTarget());
               
      if(distanceToTarget > (this.getTarget().getRadius() + this.errorDistance))
      {
         steer = super.calculateFullSteering();
      
         if(distanceToTarget < this.slowingDistance)
              steer = steer.add(this.slow.calculateFullSteering());
         
      }
      else
           this.setFreezeTheMovement(true);

      return steer;
    }
}
