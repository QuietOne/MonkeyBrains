//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviour;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.math.Plane;

import java.util.List;

/**
 * This behaviour is similar to ObstacleAvoidanceBehaviour wich the difference
 * that the obstacles can be other agents in movement. <br> <br>
 * 
 * "Unaligned collision avoidance behavior: avoid colliding with other
 *  nearby vehicles moving in unconstrained directions.  Determine which
 *  if any) other other vehicle we would collide with first, then steers
 *  to avoid the site of that potential collision.  Returns a steering
 *  force vector, which is zero length if there is no impending collision."
 * 
 * @see ObstacleAvoidanceBehaviour
 * 
 * @author Jesús Martín Berlangas
 * @version 1.1.1
 */
public class UnalignedCollisionAvoidanceBehaviour extends ObstacleAvoidanceBehaviour
{
    private static final float PARALLELNESSCHECK_ANGLE = 0.707f;
    private float distanceMultiplier = 1;
    
    /** @see ObstacleAvoidanceBehaviour#ObstacleAvoidanceBehaviour(com.jme3.ai.agents.Agent, java.util.List, float) */
    public UnalignedCollisionAvoidanceBehaviour(Agent agent, List<Agent> obstacles, float minTimeToCollision) {
        super(agent, obstacles, minTimeToCollision);
    }

    /** @see ObstacleAvoidanceBehaviour#ObstacleAvoidanceBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial, java.util.List, float)  */
    public UnalignedCollisionAvoidanceBehaviour(Agent agent, List<Agent> obstacles, float minTimeToCollision, Spatial spatial) {
        super(agent, obstacles, minTimeToCollision, spatial);
    }
    
    /** @see ObstacleAvoidanceBehaviour#ObstacleAvoidanceBehaviour(com.jme3.ai.agents.Agent, java.util.List, float, float)  */
    public UnalignedCollisionAvoidanceBehaviour(Agent agent, List<Agent> obstacles, float minTimeToCollision, float minDistance) {
        super(agent, obstacles, minTimeToCollision, minDistance);
    }

    /** @see ObstacleAvoidanceBehaviour#ObstacleAvoidanceBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial, java.util.List, float, float) */
    public UnalignedCollisionAvoidanceBehaviour(Agent agent, List<Agent> obstacles, float minTimeToCollision, float minDistance, Spatial spatial) {
        super(agent, obstacles, minTimeToCollision, minDistance, spatial);
    }
    
    /** 
     * @param distanceMultiplier Multiplies the distance required to evade an obstacle, A higher value means that will evade far obstacles
     * @see ObstacleAvoidanceBehaviour#ObstacleAvoidanceBehaviour(com.jme3.ai.agents.Agent, java.util.List, float, float)  
     */
    public UnalignedCollisionAvoidanceBehaviour(Agent agent, List<Agent> obstacles, float minTimeToCollision, float minDistance, float distanceMultiplier) {
        super(agent, obstacles, minTimeToCollision, minDistance);
        this.validateDistanceMultiplier(distanceMultiplier);
        this.distanceMultiplier = distanceMultiplier;
    }

    /** 
     * @param distanceMultiplier Multiplies the distance required to evade an obstacle, A higher value means that will evade far obstacles
     * @see ObstacleAvoidanceBehaviour#ObstacleAvoidanceBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial, java.util.List, float, float) 
     */
    public UnalignedCollisionAvoidanceBehaviour(Agent agent, List<Agent> obstacles, float minTimeToCollision, float minDistance, float distanceMultiplier, Spatial spatial) {
        super(agent, obstacles, minTimeToCollision, minDistance, spatial);
        this.validateDistanceMultiplier(distanceMultiplier);
        this.distanceMultiplier = distanceMultiplier;
    }
   
     /** @see IllegalBehaviour */
     public static class UnalignedObstacleAvoindanceWithNegativeDistanceMultiplier extends IllegalBehaviour {
        private UnalignedObstacleAvoindanceWithNegativeDistanceMultiplier(String msg) { super(msg); }
     }
     
     private void validateDistanceMultiplier(float distanceMultiplier) {
         if(distanceMultiplier < 0)
             throw new UnalignedObstacleAvoindanceWithNegativeDistanceMultiplier("The min distance from an obstacle can not be negative. Current value is " + distanceMultiplier);
     }
    
    /** @see ObstacleAvoidanceBehaviour#calculateFullSteering()  */
    @Override
    protected Vector3f calculateFullSteering() 
    {
        Vector3f steer = null;
        
        //"go on to consider potential future collisions"
        Agent threat = null;
        
        /* "Time (in seconds) until the most immediate collision threat found
           so far.  Initial value is a threshold: don't look more than this
           many frames into the future." */
        float minTime = this.getMinTimeToCollision();
        
        // xxx solely for annotation
        Vector3f xxxThreatPositionAtNearestApproach = null;
        Vector3f xxxOurPositionAtNearestApproach = null;
    
        /* "For each of the other vehicles, determine which (if any)
           pose the most immediate threat of collision." */
        for(Agent obstacle : this.getObstacles()) { if(obstacle != this.agent && obstacle.distanceRelativeToAgent(this.agent) < super.getMinDistance())
        {
           // "avoid when future positions are this close (or less)"
            // "At OpenSeer" => float collisionDangerThreshold = this.agent.getRadius() * 2;
           float collisionDangerThreshold = (this.agent.getRadius() * 2 + obstacle.getRadius() * 1.25f) * this.distanceMultiplier;
           
           // 'predicted time until nearest approach of "this" and "other"'
           float time = this.agent.predictNearestApproachTime(obstacle);
           //System.out.println("Time: " + time);//DEBUG
           
           /* "If the time is in the future, sooner than any other
           threatened collision..." */
           if ((time >= 0) && (time < minTime * (obstacle.getRadius() + this.agent.getRadius()))) // "At OpenSeer" =>  if ((time >= 0) && (time < minTime))
           {
               Vector3f threatPositionAtNearestApproach = new Vector3f();
               Vector3f ourPositionAtNearestApproach = new Vector3f();
               
                /* "if the two will be close enough to collide,
                   make a note of it" */
               if(this.agent.computeNearestApproachPositions(obstacle, time, ourPositionAtNearestApproach, threatPositionAtNearestApproach) < collisionDangerThreshold)
               {
                   minTime = time;
                   threat = obstacle;
                   xxxThreatPositionAtNearestApproach = threatPositionAtNearestApproach;
                   xxxOurPositionAtNearestApproach = ourPositionAtNearestApproach;
               }
            }
        }}
        
        // "if a potential collision was found, compute steering to avoid"
        if(threat != null)
        {   
            Vector3f agentVelocity = this.agent.getVelocity();
            Vector3f otherVelocity = threat.getVelocity();
            boolean forceTreatAsParallel = false;
                
                //If agent velocity is zero compute the behaviour as the velocities are parallel
                if(agentVelocity == null || agentVelocity.equals(Vector3f.ZERO))
                {
                    agentVelocity = new Vector3f();
                    forceTreatAsParallel = true;
                }

                //If Threat velocity is zero compute the behaviour as the velocities are parallel
                if(otherVelocity == null || otherVelocity.equals(Vector3f.ZERO))
                {
                    otherVelocity = new Vector3f();
                    forceTreatAsParallel = true;
                }
                         
            Vector3f agentVNormNeg = agentVelocity.normalize().negate();
                this.removeNegZeros(agentVNormNeg);
  
            if((otherVelocity.normalize()).equals(agentVNormNeg))
            {
               steer = randomVectInPlane(this.agent.getVelocity(), this.agent.getLocalTranslation()).normalize();
            }
            else //Check for paralleness
            {
                float parallelness = agentVelocity.normalize().dot(otherVelocity.normalize());
                
                 // "anti-parallel "head on" paths" or "parallel paths": Evade moving to the correct side
                if((parallelness < -UnalignedCollisionAvoidanceBehaviour.PARALLELNESSCHECK_ANGLE || parallelness > UnalignedCollisionAvoidanceBehaviour.PARALLELNESSCHECK_ANGLE)
                        || forceTreatAsParallel)
                {
                    Vector3f agentFordwardVector;
                    
                    if(!forceTreatAsParallel)
                        agentFordwardVector = agentVelocity.normalize();
                    else
                        agentFordwardVector = this.agent.fordwardVector();
                    
                    //Calculate side vector
                    Plane sidePlane = new Plane();
                    sidePlane.setOriginNormal(this.agent.getLocalTranslation(), agentFordwardVector);

                        Vector3f sidePoint = sidePlane.getClosestPoint(threat.getLocalTranslation());
                    Vector3f sideVector = this.agent.offset(sidePoint).normalize().negate();
                    
                    if(sideVector.negate().equals(Vector3f.ZERO)) //Move in a random direction
                        sideVector = randomVectInPlane(this.agent.getVelocity(), this.agent.getLocalTranslation()).normalize();
                    
                    steer = sideVector.mult(this.agent.getMoveSpeed());                   
                }
                
                // "perpendicular paths:"  Steer away and slow/increase the speed knowing future positions
                else steer = xxxOurPositionAtNearestApproach.subtract(xxxThreatPositionAtNearestApproach);
            }
        }
               
        if(steer == null) steer = new Vector3f();
        return steer;
    }
    
    //removes negative zeros
    private void removeNegZeros(Vector3f vec)
    {
       if(vec.x == -0) 
           vec.setX(0);
       if(vec.y == -0) 
           vec.setY(0);
       if(vec.z == -0) 
           vec.setZ(0);
    }
}
