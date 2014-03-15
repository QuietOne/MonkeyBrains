package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.AttackBehaviour;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author Tihomir Radosavljević
 */
public class SteeringMainBehaviour extends Behaviour{

    private AttackBehaviour attackBehaviour;
    private SeekBehaviour seekBehaviour;
    private Agent target;
    
    public SteeringMainBehaviour(Agent agent, Agent target) {
        super(agent);
        attackBehaviour = new AttackBehaviour(agent, null);
        seekBehaviour = new SeekBehaviour(agent, target);
        this.target = target;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        seekBehaviour.update(tpf);
        attackBehaviour.setTarget(target);
        attackBehaviour.update(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
