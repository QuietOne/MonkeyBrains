//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviour;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Arrival behavior is identical to seek while the character is far from its target. 
 * But instead of moving through the target at full speed, this behavior causes the 
 * character to slow down as it approaches the target, eventually slowing to a stop 
 * coincident with the target.
 *
 * @see SeekBehaviour
 * 
 * @author Jesús Martín Berlanga
 * @version 1.2.1
 */
public class ArriveBehaviour extends SeekBehaviour {

    private static final float ERROR_FACTOR = 0.001f;  
    private float slowingDistance;
  
    /** 
     * The slowingDistance is (0.1 * distance betwen agents) by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target) { 
        super(agent, target);
        this.slowingDistance = agent.distanceRelativeToAgent(target) * 0.25f;
    }
    
    /**
     * The slowingDistance is (0.1 * distance betwen agents) by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Agent target, Spatial spatial) {  
        super(agent, target, spatial); 
        this.slowingDistance = agent.distanceRelativeToAgent(target) * 0.25f;
    }
    
    /** 
     * The slowingDistance is (0.1 * distance betwen agents) by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos) { 
        super(agent, seekingPos);
        this.slowingDistance = agent.getLocalTranslation().subtract(seekingPos).length() * 0.1f;
    }
    
    /**
     * The slowingDistance is (0.1 * distance betwen agents) by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos, Spatial spatial) {  
        super(agent, seekingPos, spatial); 
        this.slowingDistance = agent.getLocalTranslation().subtract(seekingPos).length() * 0.1f;
    }
    
    /** 
     * @param slowingDistance Distance where the agent will start slowing
     * @throws negativeSlowingDistance If slowingDistance is lower than 0
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target, float slowingDistance) { 
        super(agent, target);
        this.validateSlowingDistance(slowingDistance);
        this.slowingDistance = slowingDistance;
    }
    
    /** 
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, float) 
     * @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Agent target, float slowingDistance, Spatial spatial) {  
        super(agent, target, spatial);
        this.validateSlowingDistance(slowingDistance);
        this.slowingDistance = slowingDistance;
    }
    
    /** 
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, float) 
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.math.Vector3f) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos, float slowingDistance) { 
        super(agent, seekingPos);
        this.validateSlowingDistance(slowingDistance);
        this.slowingDistance = slowingDistance;
    }
    
    /** 
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, float) 
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.math.Vector3f, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos, float slowingDistance, Spatial spatial) {  
        super(agent, seekingPos, spatial); 
        this.validateSlowingDistance(slowingDistance);
        this.slowingDistance = slowingDistance;
    }
    
     /** @see IllegalBehaviour */
     public static class negativeSlowingDistance extends IllegalBehaviour {
        private negativeSlowingDistance(String msg) { super(msg); }
     }
     
     private void validateSlowingDistance(float  slowingDistance) {
         if(slowingDistance < 0)
             throw new negativeSlowingDistance("The slowing distance value can not be negative. Current value is " + slowingDistance);
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
      float distanceToTarget;
      float radious = 0;
      
      if(this.getTarget() != null)
      {
           distanceToTarget = this.agent.distanceRelativeToAgent(this.getTarget());
           radious = this.getTarget().getRadius();
      }
      else if(this.getSeekingPos() != null)
          distanceToTarget = this.agent.getLocalTranslation().subtract(this.getSeekingPos()).length();
      else
          return new Vector3f(); //We dont have any target or location to arrive 
               
      if(distanceToTarget < radious + ArriveBehaviour.ERROR_FACTOR)
      {
          this.setBrakingFactor(0);
      }
      else  if(distanceToTarget < this.slowingDistance)
          this.setBrakingFactor(distanceToTarget / this.slowingDistance);
      else
          this.setBrakingFactor(1);
        
       return super.calculateFullSteering();
    }
}
