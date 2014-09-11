//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * "Approach a 'wall' (or other surface or path) and then maintain a
 *  certain offset from it" <br><br>
 * 
 * Keep in mind that this relates to wall approach not necessarily to 
 * collision detection.
 * 
 * @author Jesús Martín Berlanga
 * @version  1.1
 */
public class WallApproachBehaviour extends AbstractStrengthSteeringBehaviour {
    
    //14 tests in total
    private static enum RayTests
    { 
        RAY_TEST_X(Vector3f.UNIT_X),
        RAY_TEST_Y(Vector3f.UNIT_Y),
        RAY_TEST_Z(Vector3f.UNIT_Z),
        RAY_TEST_NEGX(Vector3f.UNIT_X.negate()),
        RAY_TEST_NEGY(Vector3f.UNIT_Y.negate()),
        RAY_TEST_NEGZ(Vector3f.UNIT_Z.negate()),
        
        RAY_TEST_XZ_UP(new Vector3f(1,1,1)),
        RAY_TEST_NEGX_Z_UP(new Vector3f(-1,1,1)),
        RAY_TEST_X_NEGZ_UP(new Vector3f(1,1,-1)),
        RAY_TEST_NEGXZ_UP(new Vector3f(-1,1,-1)),
        RAY_TEST_XZ_DOWN(new Vector3f(1,-1,1)),
        RAY_TEST_NEGX_Z_DOWN(new Vector3f(-1,-1,1)),
        RAY_TEST_X_NEGZ_DOWN(new Vector3f(1,-1,-1)),
        RAY_TEST_NEGXZ_DOWN(new Vector3f(-1,-1,-1));
    
        private Vector3f direction;
        
        private RayTests(Vector3f direction) { this.direction = direction; }
        private Vector3f getDirection() { return this.direction; }
    }
    
    /** @see IllegalBehaviourException */
    public static class WallApproachWithoutWall extends IllegalBehaviourException
    {
        private WallApproachWithoutWall(String msg) { super(msg); }
    }
    
    public static class WallApproachNegativeOffset extends IllegalBehaviourException
    {
        private WallApproachNegativeOffset(String msg) { super(msg); }
    }
    
    private Node wall;
    private float offsetToMaintain;
    private float rayTestOffset;
    
    private static final float MIN_RAY_TEST_OFFSET = 0.001f;
    
    /** @throws WallApproachNegativeOffset If offsetToMaintain is negative */
    public void setOffsetToMaintain(float offsetToMaintain) 
    { 
        WallApproachBehaviour.validateOffsetToMaintain(offsetToMaintain);
        this.offsetToMaintain = offsetToMaintain; 
    }
    
    /** 
     * @param wall Surface or path where the agent will maintain a certain offset
     * @paaram offsetToMaintain Offset from the surface that the agent will have to maintain
     * 
     * @throws WallApproachWithoutWall If the wall is a null pointer
     * @throws WallApproachNegativeOffset If offsetToMaintain is negative
     * 
     * @see AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent) 
     */
    public WallApproachBehaviour(Agent agent, Node wall, float offsetToMaintain)
    {        
         super(agent);
         WallApproachBehaviour.validateConstruction(wall, offsetToMaintain);
         this.wall = wall;
         this.offsetToMaintain = offsetToMaintain;
     
         if(offsetToMaintain != 0)
            this.rayTestOffset = (offsetToMaintain + agent.getRadius()) * 4;
         else
            this.rayTestOffset = WallApproachBehaviour.MIN_RAY_TEST_OFFSET;
    }

    /** 
     * @see WallApproach#WallApproach(com.jme3.ai.agents.Agent, com.jme3.scene.Node, float) 
     * @see AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public WallApproachBehaviour(Agent agent, Node wall, float offsetToMaintain, Spatial spatial)     
    {      
        super(agent, spatial);
        WallApproachBehaviour.validateConstruction(wall, offsetToMaintain);
        this.wall = wall;
        this.offsetToMaintain = offsetToMaintain;
        
         if(offsetToMaintain != 0)
            this.rayTestOffset = (offsetToMaintain + agent.getRadius()) * 4;
         else
            this.rayTestOffset = WallApproachBehaviour.MIN_RAY_TEST_OFFSET;
    }
    
    private static void validateConstruction(Node wall, float offsetToMaintain) 
    {
        if(wall == null) throw new WallApproachWithoutWall
                    (
                        "You can not instantiate a new wall approach behaviour without a wall."
                    );
        
        else 
            WallApproachBehaviour.validateOffsetToMaintain(offsetToMaintain);
    }
    
    private static void validateOffsetToMaintain(float offsetToMaintain)
    {
         if(offsetToMaintain < 0)  throw new WallApproachNegativeOffset
                    (
                        "The superficial offset to maintain cannot be negative. You have passed  " 
                        + offsetToMaintain + " as offset."
                    );   
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }
    
    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering() */
    @Override
    protected Vector3f calculateFullSteering() 
    { 
        Vector3f steer = new Vector3f();
        
        Vector3f aproximatedSurfaceLocationDir = this.approximateSurfaceLocation();
        
        if(aproximatedSurfaceLocationDir != null)
        {
            Vector3f surfaceLocation = this.surfaceLocation(aproximatedSurfaceLocationDir);
            
            if(surfaceLocation != null)
            {
                Vector3f extraOffset = this.agent.offset(surfaceLocation).negate().normalize().mult(this.offsetToMaintain);
                
                SeekBehaviour seek = new SeekBehaviour(this.agent, surfaceLocation.add(extraOffset));
                steer = seek.calculateFullSteering();
            }
        }
        
        return steer;
    }
    
    //Check for intersections with the wall - Ray test
    private Vector3f approximateSurfaceLocation()
    {
        class LowerDistances
        {
            private Vector3f[] lowerDistances = new Vector3f[]
                    {
                        Vector3f.POSITIVE_INFINITY,
                        Vector3f.POSITIVE_INFINITY,
                        Vector3f.POSITIVE_INFINITY,
                    };
            
            private void addDistance(Vector3f distance)
            {
                float distanceLength = distance.length();
                
                int lowerPos =  -1;
                float currentLength = Float.MAX_VALUE;
                
                for(int i = 0; i < lowerDistances.length; i++)
                {
                    float length = lowerDistances[i].length();
                    
                    if(length > currentLength)
                    {
                        currentLength = length;
                        lowerPos = i;
                    }
                }
                
                if(lowerPos != -1 && lowerDistances[lowerPos].length() > distanceLength)
                {
                    lowerDistances[lowerPos] = distance;
                }   
            }
            
            private Vector3f distanceSum()
            {
                Vector3f sum = new Vector3f();
                boolean almostOne = false;
                
                for(int i = 0; i < lowerDistances.length; i++)  if(lowerDistances[i] != null && lowerDistances[i].length() < rayTestOffset )
                {
                    sum = sum.add(lowerDistances[i]);
                    almostOne = true;
                }
                
                if(almostOne)
                    return sum;
                else
                    return null;
            }
        }
        
        LowerDistances distances = new LowerDistances();
        
        for(RayTests rayTest : WallApproachBehaviour.RayTests.values() )
        {
            Vector3f rayTestSurfaceLocation = this.surfaceLocation(rayTest.getDirection());
            
            if(rayTestSurfaceLocation != null)
                distances.addDistance(agent.offset(rayTestSurfaceLocation));
        }
        
        return distances.distanceSum();
    }
    
    private Vector3f surfaceLocation(Vector3f direction)
    {
        Vector3f surfaceLocation = null;
               
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(this.agent.getLocalTranslation(), direction);
        this.wall.collideWith(ray, results);
        
        CollisionResult collisionResult = results.getClosestCollision();
        
        if(collisionResult != null &&  !Float.isNaN(collisionResult.getDistance()) && !Float.isInfinite(collisionResult.getDistance()) )
            surfaceLocation = results.getClosestCollision().getContactPoint();

        return surfaceLocation;
    }
    
}