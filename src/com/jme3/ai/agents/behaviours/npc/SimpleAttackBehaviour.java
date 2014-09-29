package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.AgentExceptions;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.events.GameEntitySeenEvent;
import com.jme3.ai.agents.events.GameEntitySeenListener;
import com.jme3.ai.agents.util.GameEntity;
import com.jme3.ai.agents.util.weapons.AbstractFirearmWeapon;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Simple attack behaviour for NPC. This behaviour is for to agent to attack
 * targetedObject or fixed point in space. It will attack with weapon, and it
 * doesn't check if it has ammo.
 *
 * @see AbstractFirearmWeapon#hasBullets()
 * @see AbstractFirearmWeapon#isInRange(com.jme3.math.Vector3f)
 * @see AbstractFirearmWeapon#isInRange(com.jme3.ai.agents.util.GameEntity)
 *
 * @author Tihomir Radosavljević
 * @version 1.0.2
 */
public class SimpleAttackBehaviour extends Behaviour implements GameEntitySeenListener {

    /**
     * Target for attack behaviour.
     */
    protected GameEntity targetedObject;
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
    public void setTarget(GameEntity target) {
        this.targetedObject = target;
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
        if (agent.getInventory() != null) {
            try {
                if (targetPosition != null) {
                    agent.getInventory().getActiveWeapon().attack(targetPosition, tpf);
                    targetPosition = null;
                } else if (targetedObject != null && targetedObject.isEnabled()) {
                    agent.getInventory().getActiveWeapon().attack(targetedObject, tpf);
                }
            } catch (NullPointerException npe) {
                throw new AgentExceptions.WeaponNotFoundException(agent);
            }
        } else {
            throw new AgentExceptions.InventoryNotFoundException(agent);
        }
    }

    /**
     * Behaviour can automaticaly update its targetedObject with
     * GameEntitySeenEvent, if it is agent, then it check if it is in range and
     * check if they are in same team.
     *
     * @param event
     */
    public void handleGameEntitySeenEvent(GameEntitySeenEvent event) {
        if (event.getGameEntitySeen() instanceof Agent) {
            Agent targetAgent = (Agent) event.getGameEntitySeen();
            if (agent.isSameTeam(targetAgent)) {
                return;
            }
            if (!agent.getInventory().getActiveWeapon().isInRange(targetAgent)) {
                return;
            }
        }
        targetedObject = event.getGameEntitySeen();
        enabled = true;
    }

    /**
     * Method for checking is there any target that agent should attack.
     *
     * @return true if target is set
     */
    public boolean isTargetSet() {
        return targetPosition != null && targetedObject != null;
    }
}
