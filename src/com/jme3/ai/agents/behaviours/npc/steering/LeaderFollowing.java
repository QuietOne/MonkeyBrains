//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * This is similar to pursuit behaviour, but pursuiers must stay away from the pursued path.
 *
 * @author Jesús Martín Berlanga
 * @version 1.2
 */
public class LeaderFollowing extends SeekBehaviour {
    
    private float distanceToChangeFocus;
    private double minimumAngle;
    private float porcentajeSpeedWhenCorrectBehind;
    private float porcentajeSpeedDistance;
    
    /** @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent)  */
    public LeaderFollowing(Agent agent, Agent target) {
        super(agent, target);
        
        //Default values
        this.distanceToChangeFocus = 5;
        this.minimumAngle = Math.PI / 2;
        this.porcentajeSpeedWhenCorrectBehind = 0.18f;
        this.porcentajeSpeedDistance = 4;
    }
    
    /** 
     * @param distanceToChangeFocus Distance to change the focus.
     * @param minimunAngle  Minimum angle betwen the target velocity and the vehicle location.
     * @param porcentajeSpeedWhenCorrectBehind  Porcentaje of speed when the vehicle is in the correct position.
     * @param porcentajeSpeedDistance The distance factor of porcentajeSpeedWhenCorrectBehind.
     * 
     * @see PursuitBehaviour#PursuitBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent) 
     */
    public LeaderFollowing(Agent agent, Agent target, float distanceToChangeFocus, float minimunAngle,
            float porcentajeSpeedWhenCorrectBehind, float porcentajeSpeedDistance) {
        
        super(agent, target);
        
        this.distanceToChangeFocus = distanceToChangeFocus;
        this.minimumAngle = minimunAngle;
        this.porcentajeSpeedWhenCorrectBehind = porcentajeSpeedWhenCorrectBehind;
        this.porcentajeSpeedDistance = porcentajeSpeedDistance;
    }
    
    /** @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     *  @see PursuitBehaviour#PursuitBehaviour(com.jme3.ai.agents.Agent, com.jme3.ai.agents.Agent, float, float, float, float) */
    public LeaderFollowing(Agent agent, Agent target, Spatial spatial, float distanceToChangeFocus, float minimunAngle,
            float porcentajeSpeedWhenCorrectBehind, float porcentajeSpeedDistance) {
        
        super(agent, target, spatial);
        
        this.distanceToChangeFocus = distanceToChangeFocus;
        this.minimumAngle = minimunAngle;
        this.porcentajeSpeedWhenCorrectBehind = porcentajeSpeedWhenCorrectBehind;
        this.porcentajeSpeedDistance = porcentajeSpeedDistance;
    }
    
    /** @see AbstractStrengthSteeringBehaviour#calculateFullSteering()  */
    @Override
    protected Vector3f calculateSteering(){
        
        Vector3f desierdVel;
        
        float agentSpeed = this.agent.getMoveSpeed();
        Vector3f agentLocation = agent.getLocalTranslation();
        
        float targetSpeed = this.getTarget().getMoveSpeed();
        
        Vector3f targetVelocity;
        
        if(this.getTarget().getAcceleration() == null)
            targetVelocity = new Vector3f();
        else
            targetVelocity = this.getTarget().getAcceleration();
        
        Vector3f targetTrueLocation = this.getTarget().getLocalTranslation();
        
        //Calculate de desired speed
        float speedDiff = targetSpeed - agentSpeed;
        
        float desiredSpeed = (targetSpeed + speedDiff) * this.getTPF();
        
        //Vehicle distance from the true location
        float distanceFromTrueLocation = agentLocation.distance(targetTrueLocation);
        
        //Change the focus, non finite posible solutions
        double focusFactor = this.changeFocusFactor(distanceFromTrueLocation);
        
        Vector3f seekingLocation = targetTrueLocation.add(this.getTarget().getPredictedPosition().subtract(
                targetTrueLocation).mult((float) focusFactor));
        
        //Project the location you want to reach
        Vector3f projectedLocation = seekingLocation.add(targetVelocity.mult(desiredSpeed));
           
        //Angle controls
        if(distanceFromTrueLocation < this.distanceToChangeFocus){
            if(checkAngle(targetVelocity, targetTrueLocation, agentLocation))
                //If the vehicle is in the correct position, maintain it using the proper factor
                desierdVel = projectedLocation.subtract(agentLocation).normalize().mult((agentSpeed * porcentajeSpeedWhenCorrectBehind
                        * distanceFromTrueLocation)/this.porcentajeSpeedDistance);
            else{
                //If not, get out of the way
                projectedLocation = seekingLocation.add(targetVelocity.negate().mult((targetSpeed / (distanceFromTrueLocation * 0.05f))));
                desierdVel = projectedLocation.subtract(agentLocation).normalize().mult(agentSpeed);
            }
        }else
            //If is still away from the target, move normally
            desierdVel = projectedLocation.subtract(agentLocation).normalize().mult(agentSpeed);
        
        return desierdVel.subtract(velocity);
        
    }
    
    
    //Calculates the factor in order to change the focus
    private double changeFocusFactor(float distanceFromFocus){
        double factor;
        
        if(distanceFromFocus > this.distanceToChangeFocus)
            factor = 1;
        else
            factor = Math.pow((1 + distanceFromFocus/this.distanceToChangeFocus), 2);
        
        return factor;
    }
    
    //Return false if the angle is not correct.
    private boolean checkAngle(Vector3f targetVelocity,
            Vector3f targetTrueLocation,
            Vector3f vehicleLocation){
        return calculateAngle(targetVelocity, targetTrueLocation, vehicleLocation) > minimumAngle;
    }
    
    //Calculate the angle
    private float calculateAngle(Vector3f targetVelocity,
            Vector3f targetTrueLocation,
            Vector3f vehicleLocation){
        
        Vector3f fromTagetToVehicle = vehicleLocation.subtract(targetTrueLocation);
        return targetVelocity.angleBetween(fromTagetToVehicle);
    }
    
}
