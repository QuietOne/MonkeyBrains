package behaviours.npc;


import agents.Agent;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Base class for agent behaviour.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public abstract class Behaviour extends AbstractControl {

    protected Agent agent;

    public Behaviour(Agent agent) {
        this.agent = agent;
    }

    public Behaviour(Agent agent, Spatial spatial) {
        this.agent = agent;
        this.spatial = spatial;
    }
   
    // TODO : implement controlUpdate and put behaviour logic in subclasses
    
}
