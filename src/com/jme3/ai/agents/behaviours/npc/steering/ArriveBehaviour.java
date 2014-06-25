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
 * @version 1.1
 */
public class ArriveBehaviour extends SeekBehaviour {

    private final float ERROR_FACTOR = 0.001f;  
    private float slowingDistance;
    private AbstractSteeringBehaviour container;
    private boolean containerSettedUp = false;
    
    public void setContainerSettedUp(boolean containerSettedUp) { this.containerSettedUp = containerSettedUp; }
    public void setSlowingDistance(float slowingDistance) { this.slowingDistance = slowingDistance; }
    
    /** 
     * The slowingDistance is (0.1 * distance betwen agents) by default. <br> <br>
     * The slow proportion is 0.5 by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target) { 
        super(agent, target);
        this.slowingDistance = agent.distanceRelativeToAgent(target) * 0.25f;
    }
    
    /**
     * The slowingDistance is (0.1 * distance betwen agents) by default. <br> <br>
     * The slow proportion is 0.5 by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Agent target, Spatial spatial) {  
        super(agent, target, spatial); 
        this.slowingDistance = agent.distanceRelativeToAgent(target) * 0.25f;
    }
    
    /** 
     * The slowingDistance is (0.1 * distance betwen agents) by default. <br> <br>
     * The slow proportion is 0.5 by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos) { 
        super(agent, seekingPos);
        this.slowingDistance = agent.getLocalTranslation().subtract(seekingPos).length() * 0.1f;
    }
    
    /**
     * The slowingDistance is (0.1 * distance betwen agents) by default. <br> <br>
     * The slow proportion is 0.5 by default.
     * 
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos, Spatial spatial) {  
        super(agent, seekingPos, spatial); 
        this.slowingDistance = agent.getLocalTranslation().subtract(seekingPos).length() * 0.1f;
    }
    
    /** 
     * @param slowingDistance Distance where the agent will start slowing
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target, float slowingDistance) { 
        super(agent, target);
        this.slowingDistance = slowingDistance;
    }
    
    /** 
     * @param slowingDistance Distance where the agent will start slowing
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Agent target, Spatial spatial, float slowingDistance) {  
        super(agent, target, spatial); 
        this.slowingDistance = slowingDistance;
    }
    
    /** 
     * @param slowingDistance Distance where the agent will start slowing
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos, float slowingDistance) { 
        super(agent, seekingPos);
        this.slowingDistance = slowingDistance;
    }
    
    /** 
     * @param slowingDistance Distance where the agent will start slowing
     * @see ArriveBehaviour#ArriveBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public ArriveBehaviour(Agent agent, Vector3f seekingPos, Spatial spatial, float slowingDistance) {  
        super(agent, seekingPos, spatial); 
        this.slowingDistance = slowingDistance;
    }
   
    private void setUpContainer()
    {
        if(this.getIsAPartialSteer())
            this.container = this.getContainer();
        else
            this.container = this;  
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
      if(!this.containerSettedUp)
          this.setUpContainer();
        
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
               
      if(distanceToTarget < radious + this.ERROR_FACTOR)
      {
          this.container.setVelocityStrength(0);
      }
      else  if(distanceToTarget < this.slowingDistance)
          this.container.setVelocityStrength(distanceToTarget / this.slowingDistance);
      else
          this.container.setVelocityStrength(1);
        
       return super.calculateFullSteering();
    }
}
