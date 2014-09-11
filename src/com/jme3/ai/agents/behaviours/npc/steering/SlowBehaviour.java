//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviour;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Slows down the velocity produced by a behaviour container (g.e. CompoundSteeringBehaviour)
 * 
 * @see CompoundSteeringBehaviour
 * @see com.jme3.ai.agents.behaviours.npc.SimpleMainBehaviour
 * 
 * @author Jesús Martín Berlanga
 * @version 2.1
 */
public class SlowBehaviour extends AbstractStrengthSteeringBehaviour {

    private int timeInterval;
    private float percentajeSlow;
    private float maxBrakingFactor = 1;
    
    public void setMaxBrakingFactor(float maxBrakingFactor) { this.maxBrakingFactor = maxBrakingFactor; }
    
    /** @param percentajeSlow float in the interval [0, 1] */
    public void setPercentajeSlow(float percentajeSlow) 
    { //Auto adjust invalid inputs
        
        if(percentajeSlow > 1)
            this.percentajeSlow = 1;
        else if(percentajeSlow < 0)
            this.percentajeSlow = 0;
        else
            this.percentajeSlow = percentajeSlow;
    }
    
    private float getBrakingFactorWrapper() { return this.getBrakingFactor(); }
    private void setBrakingFactorWrapper(float brakingFacor) { this.setBrakingFactor(brakingFacor); }
    
    private ActionListener slowIteration = new ActionListener()
    {
        public void actionPerformed(ActionEvent event)
        {
            float newStrength = getBrakingFactorWrapper() * (1 - percentajeSlow);
            
            if(newStrength < maxBrakingFactor)
                 setBrakingFactorWrapper(newStrength);
        }
    };
    
    private Timer iterationTimer;
   
    /**
     * Turns on or off the slow behaviour
     * 
     * @param active 
     */
    public void setAcive(boolean active)
    {
        if(active && !this.iterationTimer.isRunning())
            this.iterationTimer.start();
        else if (!active && this.iterationTimer.isRunning())
            this.iterationTimer.stop();
    }
    
    /**
     * Reset the slow behaviour
     */
    public void reset() { this.setBrakingFactor(1); }
    
    /**
     * Slows a steer behaviour resultant velocity.
     * 
     * @param behaviour Steer behaviour
     * @param timeInterval How much time for each slow iteration in ns
     * @param percentajeSlow What percentaje will be reduced the vecocity for each iteration, a float betwen 0 and 1
     * 
     * @throws ObstacleAvoindanceWithoutTimeInterval If time interval is not a positive integer
     * 
     * @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent) 
     */
    public SlowBehaviour(Agent agent, int timeInterval, float percentajeSlow) { 
        super(agent);
        this.construct(timeInterval, percentajeSlow);
    }
    
    /**
     * @see SlowBehaviour#SlowBehaviour(com.jme3.ai.agents.Agent, int, float) 
     * @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial) 
     */
    public SlowBehaviour(Agent agent, int timeInterval, float percentajeSlow, Spatial spatial) { 
        super(agent, spatial);
        this.construct(timeInterval, percentajeSlow);
    }
    
    /** @see IllegalBehaviour */
    public static class ObstacleAvoindanceWithoutTimeInterval extends IllegalBehaviour {
        private ObstacleAvoindanceWithoutTimeInterval(String msg) { super(msg); }
    }
    
    private void construct(int timeInterval, float percentajeSlow)
    {
        if(timeInterval <= 0)
             throw new ObstacleAvoindanceWithoutTimeInterval("The time interval must be postitive. The current value is: " + timeInterval);
        
        this.timeInterval = timeInterval;
        
        if(percentajeSlow > 1)
            this.percentajeSlow = 1;
        else if(percentajeSlow < 0)
            this.percentajeSlow = 0;
        else
            this.percentajeSlow = percentajeSlow;
        
        this.iterationTimer = new Timer(this.timeInterval, this.slowIteration); 
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

    @Override
    protected Vector3f calculateFullSteering() { return Vector3f.ZERO; }
}
