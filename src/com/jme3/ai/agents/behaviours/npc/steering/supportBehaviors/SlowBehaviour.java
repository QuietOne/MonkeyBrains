//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering.supportBehaviors;
import com.jme3.ai.agents.behaviours.npc.steering.AbstractSteeringBehaviour;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Slows down the velocity produced by a behaviour. This will not work with
 * partial behaviours. You have to apply this behaviour to the behaviour
 * that you will add (g.e. CompoundSteeringBehaviour) to a SimpleMainBehaviour.
 * 
 * @see CompoundSteeringBehaviour
 * @see com.jme3.ai.agents.behaviours.npc.SimpleMainBehaviour
 * 
 * @author Jesús Martín Berlanga
 * @version 1.1
 */
public class SlowBehaviour {

    public static final float DEFAULT_PERCENTAJE_SLOW = 0.01f;
    public static final int DEFAULT_TIME_INTERVAL = 16; //DEFAULT_TIME_INTERVAL = 16 => 62,5 intervals in a Second
    
    private AbstractSteeringBehaviour behaviour;
    private int timeInterval = DEFAULT_TIME_INTERVAL;
    private float percentajeSlow = DEFAULT_PERCENTAJE_SLOW;
    private float minVelStrength = 0;
    
    public void setMinVelStrength(float minVelStrength) { this.minVelStrength = minVelStrength; }
    
    /** @return The actual velocity strengh of the asociated behaviour */
    public float getBehaviourVelocityStrength() { return this.behaviour.getVelocityStrength(); }
    
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
    
    private ActionListener slowIteration = new ActionListener()
    {
        public void actionPerformed(ActionEvent event)
        {
            float newStrength = behaviour.getVelocityStrength() * (1 - percentajeSlow);
            
            if(newStrength >= minVelStrength)
                 behaviour.setVelocityStrength(newStrength);
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
    public void reset() { this.behaviour.setVelocityStrength(1); }
    
    /**
     * Slows a steer behaviour resultant velocity. For each iteration the
     * velocity will be reduced a 2.5%. The time interval for each iteration
     * is 95ns by default.
     * 
     * @param behaviour Steer behaviour
     */
    public SlowBehaviour(AbstractSteeringBehaviour behaviour) { 
        this.behaviour = behaviour; 
        
        this.iterationTimer = new Timer(this.timeInterval, this.slowIteration);
    }
    
    
    /**
     * Slows a steer behaviour resultant velocity. For each iteration the
     * velocity will be reduced a 2.5%.
     * 
     * @param behaviour Steer behaviour
     * @param timeInterval How much time for each slow iteration in ns
     */
    public SlowBehaviour(AbstractSteeringBehaviour behaviour, int timeInterval) { 
        this.behaviour = behaviour; 
        this.timeInterval = timeInterval;
        
        this.iterationTimer = new Timer(this.timeInterval, this.slowIteration);
    }
    
    /**
     * Slows a steer behaviour resultant velocity.
     * 
     * @param behaviour Steer behaviour
     * @param timeInterval How much time for each slow iteration in ns
     * @param percentajeSlow What percentaje will be reduced the vecocity for each iteration, a float betwen 0 and 1
     */
    public SlowBehaviour(AbstractSteeringBehaviour behaviour, int timeInterval, float percentajeSlow) { 
        this.behaviour = behaviour; 
        this.timeInterval = timeInterval;
        
        if(percentajeSlow > 1)
            this.percentajeSlow = 1;
        else if(percentajeSlow < 0)
            this.percentajeSlow = 0;
        else
            this.percentajeSlow = percentajeSlow;
        
        this.iterationTimer = new Timer(this.timeInterval, this.slowIteration);
    }
    
}
