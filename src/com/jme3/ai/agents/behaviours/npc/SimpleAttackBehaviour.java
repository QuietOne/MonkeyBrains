package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.events.GameObjectSeenEvent;
import com.jme3.ai.agents.events.GameObjectSeenListener;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.ai.agents.util.AbstractWeapon;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Simple attack behaviour for NPC. This behaviour is for to agent to attack
 * target. It will attack with weapon, and it doesn't check if it has ammo, or
 * if target is in range.
 *
 * @see AbstractWeapon#hasBullets()
 * @see AbstractWeapon#isInRange(com.jme3.math.Vector3f)
 * @see AbstractWeapon#isInRange(com.jme3.ai.agents.util.GameObject)
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimpleAttackBehaviour extends Behaviour implements GameObjectSeenListener {

    /**
     * Target for attack behaviour.
     */
    private Vector3f target;

    /**
     * @param agent to whom behaviour belongs
     */
    public SimpleAttackBehaviour(Agent agent) {
        super(agent);
    }

    /**
     * @param agent to whom behaviour belongs
     * @param spatial active spatial durring this behaviour
     */
    public SimpleAttackBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
    }

    /**
     * @param target at which agent will shoot
     */
    public void setTarget(Vector3f target) {
        this.target = target;
    }

    /**
     * @param target at which agent will shoot
     */
    public void setTarget(GameObject target) {
        this.target = target.getLocalTranslation();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (agent.getWeapon() != null && target != null) {
            agent.getWeapon().attack(target, tpf);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("You should override it youself");
    }

    /**
     * Behaviour can automaticaly update its target with GameObjectSeenEvent.
     *
     * @param event
     */
    public void handleGameObjectSeenEvent(GameObjectSeenEvent event) {
        target = event.getGameObjectSeen().getLocalTranslation();
        enabled = true;
    }
}
