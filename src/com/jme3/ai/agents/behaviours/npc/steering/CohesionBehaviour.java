//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 * Move toward center of neighbors.
 *
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class CohesionBehaviour extends AbstractStrengthSteeringBehaviour {
    
    private List<Agent> neighbours = new ArrayList<Agent>();
    
    public List<Agent> getNeighbours(){return this.neighbours;}
    
    private float maxDistance = Float.POSITIVE_INFINITY;
    private float maxAngle = FastMath.PI / 2;
    
    /**
     *  maxAngle is setted to PI / 2 by default and maxDistance to infinite.
     * 
     * @param agent To whom behaviour belongs.
     * @param neighbours Neighbours, this agent is moving toward the center of this neighbours.
     */
    public CohesionBehaviour(Agent agent, List<Agent> neighbours){
        super(agent);
        this.neighbours = neighbours;
    }
    
    /**
     * @param maxDistance In order to consider a neihbour inside the neighbourhood
     * @param maxAngle In order to consider a neihbour inside the neighbourhood
     * @see  Agent#inBoidNeighborhoodMaxAngle(com.jme3.ai.agents.Agent, float, float, float) 
     * @see  CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent, java.util.List) 
     */
    public CohesionBehaviour(Agent agent, List<Agent> neighbours, float maxDistance, float maxAngle){
        super(agent);
        this.neighbours = neighbours;
        this.maxDistance = maxDistance;
        this.maxAngle = maxAngle;
    }
    
    /**
     * @param spatial active spatial during excecution of behaviour
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent, java.util.List)
     */
    public CohesionBehaviour(Agent agent, Spatial spatial, List<Agent> neighbours) {
        super(agent, spatial);
        this.neighbours = neighbours;
    }
    
    /**
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent, java.util.List)
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent, java.util.List, float, float) 
     */
     public CohesionBehaviour(Agent agent, Spatial spatial, List<Agent> neighbours, float maxDistance, float maxAngle) {
        super(agent, spatial);
        this.neighbours = neighbours;
        this.maxDistance = maxDistance;
        this.maxAngle = maxAngle;
     }
    
    @Override
    protected Vector3f calculateFullSteering()
    {
        
        // steering accumulator and count of neighbors, both initially zero
        Vector3f steering = new Vector3f();

        int realNeighbors = 0; 
        
        // for each of the other vehicles...
        for (Agent agentO : this.neighbours)
        {
            if (this.agent.inBoidNeighborhood(agentO, this.agent.getRadius()*3, this.maxDistance, this.maxAngle))
            {
                // accumulate sum of neighbor's positions
                steering = steering.add(agentO.getLocalTranslation());
                realNeighbors++;
            }
        }
        
        // divide by neighbors, subtract off current position to get error-correcting direction
       if(realNeighbors > 0)
        {
             steering = steering.divide(realNeighbors);
             steering = this.agent.offset(steering);
        } 
        
        return steering;
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {  }
  
}
