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
 * "The queuing results from a steering behavior which produces braking (deceleration)
 *  when the vehicle detects other vehicles which are: nearby, in front of, and moving
 *  slower than itself." <br> <br>
 * 
 *  In most cases you will combine this behaviour with seek, separation and/or 
 *  avoid. Queuing is designed to be combined with the behaviours mentioned before
 *  in order to to leave a large 'room' through a narrow 'doorway': "drawn toward 
 *  the 'doorway' by seek behavior, avoid walls, and maintain separation from each other."
 * 
 *  @author Jesús Martín Berlanga
 *  @version 1.0
 */
public class QueuingBehaviour extends AbstractStrengthSteeringBehaviour {

    private List<Agent> neighbours = new ArrayList<Agent>();
    public List<Agent> getNeighbours(){return this.neighbours; }
    private float minDistance;
    
    private AbstractSteeringBehaviour container;
    private boolean containerSettedUp = false;
    
    public void setContainerSettedUp(boolean containerSettedUp) { this.containerSettedUp = containerSettedUp; }
   
    /** 
     * @param neighbours Queue of agents
     * @param minDistance Min. distance from center to center to consider a neighbour as an obstacle
     * @see AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent)   
     */
    public QueuingBehaviour(Agent agent, List<Agent> neighbours, float minDistance) {
         super(agent);
         this.neighbours = neighbours;
         this.minDistance = minDistance;
    }

     /** 
      * @see QueuingBehaviour#QueuingBehaviour(com.jme3.ai.agents.Agent, java.util.List, float) 
      * @see AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)  
      */
    public QueuingBehaviour(Agent agent, List<Agent> neighbours, float minDistance, Spatial spatial) {
        super(agent, spatial);
        this.neighbours = neighbours;
        this.minDistance = minDistance;
    }
    
    private void setUpContainer()
    {
        if(this.getIsAPartialSteer())
            this.container = this.getContainer();
        else
            this.container = this;
        
        this.containerSettedUp = true;
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }
    
    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering() */
    @Override
    protected Vector3f calculateFullSteering() 
    { 
        if(!this.containerSettedUp)
          this.setUpContainer();
              
        Vector3f agentVelocity = this.agent.getVelocity();
        
        int numberObstacles = 1;
        float distanceFactor = 1;
        float velocityFactor = 1;
        
        if(agentVelocity != null && !agentVelocity.equals(Vector3f.ZERO))
        for(Agent neighbour : this.neighbours) 
        {
            Vector3f neighVel = neighbour.getVelocity();
            float fordwardness = this.agent.forwardness(neighbour);
            float velDiff;
            float distance;
             
            if
            (
              neighbour != this.agent &&
              (distance = this.agent.distanceRelativeToAgent(neighbour)) < this.minDistance &&
              fordwardness > 0 && 
              neighVel != null &&
              (velDiff = neighVel.length() - agentVelocity.length()) < 0
            )
            {
                distanceFactor *= distance / this.minDistance;
                velocityFactor *= -velDiff  / this.agent.getMoveSpeed();
                numberObstacles++;
            }
        }
        
        this.container.setVelocityStrength((distanceFactor + velocityFactor + (1/numberObstacles)) /3);
        
        return new Vector3f();
    }
    
}