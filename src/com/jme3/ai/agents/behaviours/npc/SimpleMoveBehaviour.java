package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Simple move behaviour for NPC. Agent should move to targeted position or to
 * moveDirection. If both are added then agent will move to targeted position.
 * <br>Warrning:<br>
 * Agent sometimes will never move exactly to targeted position if moveSpeed is
 * too high so add appropriate distance error.
 *
 * @see Agent#moveSpeed
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimpleMoveBehaviour extends Behaviour {

    /**
     * Targeted position.
     */
    private Vector3f targetPosition;
    /**
     * Move direction of agent.
     */
    private Vector3f moveDirection;
    /**
     * Distance of targetPosition that is acceptable.
     */
    private float distanceError;

    public SimpleMoveBehaviour(Agent agent) {
        super(agent);
        distanceError = 0;
    }

    public SimpleMoveBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        distanceError = 0;
    }

    @Override
    protected void controlUpdate(float tpf) {
        //if there is target position where agent should move
        if (targetPosition != null) {
            if (agent.getLocalTranslation().distance(targetPosition) <= distanceError) {
                targetPosition = null;
                moveDirection = null;
                enabled = false;
                return;
            }
            moveDirection = targetPosition.subtract(agent.getLocalTranslation()).normalize();
        }
        //if there is movement direction in which agent should move
        if (moveDirection != null) {
            agent.getSpatial().move(moveDirection.mult(agent.getMoveSpeed() * tpf));
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("You should override it youself");
    }

    /**
     * @return position of target
     */
    public Vector3f getTargetPosition() {
        return targetPosition;
    }

    /**
     * @param targetPosition position of target
     */
    public void setTargetPosition(Vector3f targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     *
     * @return movement vector
     */
    public Vector3f getMoveDirection() {
        return moveDirection;
    }

    /**
     *
     * @param moveDirection movement vector
     */
    public void setMoveDirection(Vector3f moveDirection) {
        this.moveDirection = moveDirection.normalize();
    }

    /**
     *
     * @return allowed distance error
     */
    public float getDistanceError() {
        return distanceError;
    }

    /**
     *
     * @param distanceError allowed distance error
     */
    public void setDistanceError(float distanceError) {
        this.distanceError = distanceError;
    }
}
