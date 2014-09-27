package com.jme3.ai.agents.util.weapons;

/**
 *
 * @author Psesgato
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public interface AbstractPowerSource {

    public boolean contains(int energyRequest);
    
    public void consume(int energyRequest);
}
