//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Slows down the agent velocity in a proportion setted by the user.
 * 
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class SlowBehaviour extends AbstractStrengthSteeringBehaviour {
    
    private float maxSlow; //It is a number that work well, Do not chage it.
    private float unitIncrementSlow = 0.0035f;
    private float maxPercentajeSlow = 1; //From 0 to 1
    private float actualSlow = 0;
    
    /**
     * Sets actualSlow to 0
     */
    public void resetSlow() { this.actualSlow = 0; }
    
    /** @param maxPercentajeSlow Float betwen 0 and 1 */
    public void setMaxPercentajeslow(float maxPercentajeSlow)
    {
        if(maxPercentajeSlow <= 1)
            this.maxPercentajeSlow = maxPercentajeSlow;
        else
            this.maxPercentajeSlow = 1;
    }
    
    public void setUnitIncrementSlow(float unitIncrementSlow) { 
        
        if(unitIncrementSlow <= this.maxSlow && unitIncrementSlow >= 0)
            this.unitIncrementSlow = unitIncrementSlow; 
        else
            this.unitIncrementSlow = this.maxSlow;
        
    }
    
    /** 
     * The unit increment proportion is 0.0035 by default.
     * @see  AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent) 
     */
    public SlowBehaviour(Agent agent) { 
        super(agent); 
        this.maxSlow = agent.getMoveSpeed() * 10;
    }
    
    /** 
     * The unit increment proportion is 0.0035 by default.
     * @see AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public SlowBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        this.maxSlow = agent.getMoveSpeed() * 10;
    }
    
    /** 
     * @param  unitIncrementSlow The unit increment proportion 
     * @see AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public SlowBehaviour(Agent agent, Spatial spatial, float unitIncrementSlow) { 
        super(agent, spatial);
        this.unitIncrementSlow = unitIncrementSlow;
        this.maxSlow = agent.getMoveSpeed() * 10;
    }
    
    /** 
     * @param  unitIncrementSlow The unit increment proportion 
     * @see  AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent) 
     */
    public SlowBehaviour(Agent agent, float unitIncrementSlow) { 
        super(agent);
        this.unitIncrementSlow = unitIncrementSlow;
        this.maxSlow = agent.getMoveSpeed() * 10;
    }
    
    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering() */
    @Override
    Vector3f calculateFullSteering()
    {
      Vector3f steer = new Vector3f(); 

       if(this.agent.getVelocity() != null && this.agent.getVelocity().lengthSquared() > 0)
       {
          steer = this.agent.getVelocity().negate().mult(actualSlow);
          
          if(this.actualSlow < (this.maxSlow * this.maxPercentajeSlow))
          {
             this.actualSlow += this.unitIncrementSlow;
              
              if(this.actualSlow > (this.maxSlow * this.maxPercentajeSlow))
                  this.actualSlow = (this.maxSlow * this.maxPercentajeSlow);
          }
       }
       
      this.maxSlow = this.agent.getMoveSpeed() * 10; //Update max slow

      return steer;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

}
