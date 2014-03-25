package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example of attack behaviour fon NPC.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class AttackBehaviour extends Behaviour{

    /**
     * Targeted agent.
     */
    private Agent target;

    public AttackBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
    }

    public void setTarget(Agent target) {
        this.target = target;
    }
    
    // the main logic of behaviour goes here 
    @Override
    protected void controlUpdate(float tpf) {
        if (agent.getWeapon().getBullet() == null) {
            try {
                if (target != null) {
                    if (!target.isAlive() || !agent.getWeapon().isInRange(target)) {
                        target = null;
                        return;
                    }
                }
                agent.getWeapon().shootAt(target, tpf);
            } catch (Exception ex) {
                //Logger.getLogger(AttackBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

}
