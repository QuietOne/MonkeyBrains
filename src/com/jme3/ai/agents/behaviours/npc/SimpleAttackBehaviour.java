package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.events.PhysicalObjectSeenEvent;
import com.jme3.ai.agents.events.PhysicalObjectSeenListener;
import com.jme3.ai.agents.util.PhysicalObject;
import com.jme3.ai.agents.util.AbstractWeapon;
import com.jme3.ai.agents.util.control.Game;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Simple attack behaviour for NPC. This behaviour is for to agent to attack
 * targetObject or fixed point in space. It will attack with weapon, and it
 * doesn't check if it has ammo.
 *
 * @see AbstractWeapon#hasBullets()
 * @see AbstractWeapon#isInRange(com.jme3.math.Vector3f)
 * @see AbstractWeapon#isInRange(com.jme3.ai.agents.util.PhysicalObject)
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimpleAttackBehaviour extends Behaviour implements PhysicalObjectSeenListener {

    /**
     * Target for attack behaviour.
     */
    protected PhysicalObject targetObject;
    /**
     * Target for attack behaviour.
     */
    protected Vector3f targetPosition;

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
     * @param target at which agent will attack
     */
    public void setTarget(PhysicalObject target) {
        this.targetObject = target;
    }

    /**
     *
     * @param target at what point will agent attack
     */
    public void setTarget(Vector3f target) {
        this.targetPosition = target;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (agent.getWeapon() != null) {
            if (targetPosition != null) {
                agent.getWeapon().attack(targetPosition, tpf);
                targetPosition = null;
            } else if (targetObject != null && targetObject.isEnabled()) {
                agent.getWeapon().attack(targetObject, tpf);
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("You should override it youself");
    }

    /**
     * Behaviour can automaticaly update its targetObject with
     * PhysicalObjectSeenEvent, if it is agent, then it check if it is in range and
     * check if they are in same team.
     *
     * @param event
     */
    public void handlePhysicalObjectSeenEvent(PhysicalObjectSeenEvent event) {
        if (event.getPhysicalObjectSeen() instanceof Agent) {
            Agent targetAgent = (Agent) event.getPhysicalObjectSeen();
            if (agent.isSameTeam(targetAgent)) {
                return;
            }
            if (!agent.getWeapon().isInRange(targetAgent)) {
                return;
            }
        }
        targetObject = event.getPhysicalObjectSeen();
        enabled = true;
    }
}
