//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * This is similar to pursuit behaviour, but pursuiers must stay away from the pursued path. 
 * In order to stay away from that path the pursuers resort to "evade".
 * Furthermore pursuers use "arrive" instead of "seek" to approach to their objective.
 *
 * @see PursuitBehaviour
 * 
 * @author Jesús Martín Berlanga
 * @version 1.3
 */
public class LeaderFollowing extends SeekBehaviour {
       
    private float distanceToChangeFocus;
    private float distanceToEvade;
    private float minimumAngle;
    private ArriveBehaviour arriveBehaviour;
    private EvadeBehaviour evadeBehaviour;
    private boolean containerSettedUp = false;
    
    public void setContainerSettedUp(boolean containerSettedUp) { this.containerSettedUp = containerSettedUp; }
       
    /** @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent)  */
    public LeaderFollowing(Agent agent, Agent target)
    {
        super(agent, target);
        this.evadeBehaviour = new EvadeBehaviour(agent, target);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);
        
        //Default values
        this.distanceToEvade = 2;
        this.distanceToChangeFocus = 5;
        this.minimumAngle = FastMath.PI / 2.35f;
    }
    
    /** @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)   */
    public LeaderFollowing(Agent agent, Agent target, Spatial spatial)
    {
        super(agent, target, spatial);
        this.evadeBehaviour = new EvadeBehaviour(agent, target, spatial);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);
        
        //Default values
        this.distanceToEvade = 2;
        this.distanceToChangeFocus = 5;
        this.minimumAngle = FastMath.PI / 2.35f;
    }
    
    /** 
     * @param distanceToChangeFocus Distance to change the focus.
     * @param minimunAngle  Minimum angle betwen the target velocity and the vehicle location.
     * 
     * @see LeaderFollowing#LeaderFollowing(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public LeaderFollowing(Agent agent, Agent target, float distanceToEvade,float distanceToChangeFocus, float minimunAngle) 
    {
        super(agent, target);
        this.distanceToEvade = distanceToEvade;
        this.evadeBehaviour = new EvadeBehaviour(agent, target);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);
        this.distanceToChangeFocus = distanceToChangeFocus;
        this.minimumAngle = minimunAngle;
    }
    
    /**
     *  @see LeaderFollowing#LeaderFollowing(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     *  @see LeaderFollowing#LeaderFollowing(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, float, float, float) 
     */
    public LeaderFollowing(Agent agent, Agent target, Spatial spatial, float distanceToEvade, float distanceToChangeFocus, float minimunAngle) 
    {
        super(agent, target, spatial);
        this.distanceToEvade = distanceToEvade;
        this.evadeBehaviour = new EvadeBehaviour(agent, target, spatial);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);
        this.distanceToChangeFocus = distanceToChangeFocus;
        this.minimumAngle = minimunAngle;
    }
    
    private void setupArriveContainer()
    {
        if(this.getIsAPartialSteer())
        {
            this.arriveBehaviour.setContainer(this.getContainer());
            this.arriveBehaviour.setIsAPartialSteer(true);
        }
        else
            this.arriveBehaviour.setContainer(this);
    }
    
    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering()  */
    @Override
    protected Vector3f calculateFullSteering() 
    {
         if(!this.containerSettedUp)
             this.setupArriveContainer();
        
         Vector3f steer;
         float distanceBetwen = this.agent.distanceRelativeToAgent(this.getTarget());


         //See how far ahead we need to leed
         Vector3f fullProjectedLocation = this.getTarget().getPredictedPosition();
         Vector3f predictedPositionDiff = fullProjectedLocation.subtract(this.getTarget().getLocalTranslation());
         Vector3f projectedLocation = this.getTarget().getLocalTranslation().add(predictedPositionDiff.mult(
            this.calculateFocusFactor(distanceBetwen)));
        
         this.arriveBehaviour.setSeekingPos(projectedLocation);
           
         steer = this.arriveBehaviour.calculateFullSteering();
           
         if(!(distanceBetwen > this.distanceToEvade) && !(this.getTarget().forwardness(this.agent) < FastMath.cos(this.minimumAngle))) 
         { //Incorrect angle and Is in the proper distance to evade -> Evade the leader
             
            Vector3f arriveSteer = steer.mult(distanceBetwen / this.distanceToEvade);
            Vector3f evadeSteer = this.evadeBehaviour.calculateFullSteering();
            evadeSteer.mult( this.distanceToEvade / (1 + distanceBetwen));
            steer = (new Vector3f()).add(arriveSteer).add(evadeSteer);
         }

        return steer;
    }
    
    
    //Calculates the factor in order to change the focus
    private float calculateFocusFactor(float distanceFromFocus)
    {
        float factor;
        
        if(distanceFromFocus > this.distanceToChangeFocus)
            factor = 1;
        else
            factor = FastMath.pow((1 + distanceFromFocus/this.distanceToChangeFocus), 2);
        
        return factor;
    }
    
}