//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

/**
 * "Hide behavior involves identifying a target location which is on the opposite 
 * side of an obstacle from the opponent, and steering toward it using seek." <br><br>
 * 
 * First of all we check which is the nearest obstacle with a radius equal or lower 
 * than the agent, then we hide behind it.
 * 
 * @author Jesús Martín Berlanga
 * @version 1.0
 */
public class HideBehaviour extends AbstractStrengthSteeringBehaviour {

    private float separationFromObstacle;
    private Agent target;
    
    private List<Agent> obstacles = new ArrayList<Agent>();
    public void setObstacles(List<Agent> obstacles){this.obstacles = obstacles;}
    
    /**
     * @param obstacles Obstacles that this agent will use to hide from the target
     * @param separationFromObstacle Distance from the obstacle surface that this agent will maintain
     * 
     * @throws negativeSeparationFromObstacle If separationFromObstacle is lower than 0
     * @throws HideWithoutTarget If target is null
     * 
     * @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent) 
     */
    public HideBehaviour(Agent agent, Agent target, List<Agent> obstacles, float separationFromObstacle) {
        super(agent);
        this.validateTarget(target);
        this.validateSeparationFromObstacle(separationFromObstacle);
        this.target = target;
        this.obstacles = obstacles;
        this.separationFromObstacle = separationFromObstacle;
    }
    
    /**
     * @see HideBehaviour#HideBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, java.util.List, float) 
     * @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public HideBehaviour(Agent agent, Agent target, List<Agent> obstacles, float separationFromObstacle, Spatial spatial) {
        super(agent, spatial);
        this.validateTarget(target);
        this.validateSeparationFromObstacle(separationFromObstacle);
        this.target = target;
        this.obstacles = obstacles;
        this.separationFromObstacle = separationFromObstacle;
    }
    
     /** @see IllegalBehaviourException */
     public static class HideWithoutTarget extends IllegalBehaviourException {
         private HideWithoutTarget(String msg) { super(msg); }
     }

     private void validateTarget(Agent target) {
         if(target == null) throw new HideWithoutTarget("The target can not be null.");
     }
    
     /** @see IllegalBehaviourException */
     public static class negativeSeparationFromObstacle extends IllegalBehaviourException {
        private negativeSeparationFromObstacle(String msg) { super(msg); }
     }
     
     private void validateSeparationFromObstacle(float separationFromObstacle) {
         if(separationFromObstacle < 0)
             throw new negativeSeparationFromObstacle("The separation distance from the obstacle can not be negative. Current value is " + separationFromObstacle);
     }
    
    /**  @see AbstractStrengthSteeringBehaviour#calculateFullSteering() */
    @Override
    protected Vector3f calculateFullSteering() 
    {
        Vector3f steer = Vector3f.ZERO;
        
        Agent closestObstacle = null;
        float closestDistanceFromAgent = Float.POSITIVE_INFINITY;
        
        for(Agent obstacle : this.obstacles) if(obstacle != this.agent)
        {
            float distanceFromAgent = this.agent.distanceRelativeToGameObject(obstacle);
            
            if(distanceFromAgent < closestDistanceFromAgent && obstacle.getRadius() >= this.agent.getRadius())
            {
                closestObstacle = obstacle;
                closestDistanceFromAgent = distanceFromAgent;
            }
        }
        
        if(closestObstacle != null && this.agent.distanceRelativeToGameObject(closestObstacle) > closestObstacle.getRadius())
        {
            Vector3f targetToObstacleOffset = this.target.offset(closestObstacle);
            Vector3f seekPos = this.target.getLocalTranslation().add(targetToObstacleOffset).add (
                    targetToObstacleOffset.normalize().mult(this.separationFromObstacle)
                    );
            
            SeekBehaviour seek = new SeekBehaviour(this.agent, seekPos);
            return seek.calculateFullSteering();
        }
        
        return steer;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }
}