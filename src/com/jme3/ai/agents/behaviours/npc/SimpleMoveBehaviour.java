package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.ai.agents.events.AgentSeenEvent;
import com.jme3.ai.agents.events.AgentSeenEventListener;
import com.jme3.ai.agents.util.Game;
import test.Model;

/**
 * Example of move behaviour for NPC.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class MoveBehaviour extends Behaviour implements AgentSeenEventListener{

    /**
     * Targeted agent.
     */
    private Agent target;
    /**
     * Size of terrain on which agents move.
     */
    private float terrainSize;
    
    public MoveBehaviour(Agent agent, Spatial spatial, float terrainSize) {
        super(agent, spatial);
        this.terrainSize = terrainSize;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if (target == null) {
            float turnSpeed = ((Model) agent.getModel()).getTurnSpeed();
            //if there is no target, rotate
            agent.getSpatial().rotate(0, (FastMath.DEG_TO_RAD * tpf) * turnSpeed, 0);
        } else {
            //if agent in range do not move closer to him or
            //agent is not lookable then tere is no target
            if (agent.getWeapon().isInRange(target) || !Game.getInstance().lookable(agent, target, FastMath.QUARTER_PI)) {
                target = null;
                return;
            }
            Vector3f oldPos = agent.getSpatial().getLocalTranslation().clone();
            agent.getSpatial().move(agent.getLocalRotation().mult(new Vector3f(0, 0, agent.getMoveSpeed() * tpf)));
            if (agent.getLocalTranslation().x > terrainSize * 2 || agent.getLocalTranslation().z > terrainSize * 2
                    || agent.getLocalTranslation().x < -terrainSize * 2 || agent.getLocalTranslation().z < -terrainSize * 2) {
                agent.setLocalTranslation(oldPos);
                // if enemy hit border make him move backwards
            }

        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

    public void handleAgentSeenEvent(AgentSeenEvent event) {
        // if another agent is seen set him as target
        target = event.getAgentSeen();
    }

    
}
