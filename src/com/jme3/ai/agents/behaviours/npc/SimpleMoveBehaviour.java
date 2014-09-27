package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.steering.MoveBehaviour;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Simple move behaviour for NPC. Agent should move to targeted position or to
 * moveDirection. If both are added then agent will move to targeted position.
 * <br>Warrning:<br>
 * Agent sometimes will never move exactly to targeted position if moveSpeed is
 * too high so add appropriate distance error.
 *
 * @see Agent#moveSpeed
 * @see MoveBehaviour
 * 
 * @author Tihomir Radosavljević
 * @version 1.0.1
 * @deprecated
 */
public class SimpleMoveBehaviour extends Behaviour {

    /**
     * Targeted position.
     */
    protected Vector3f targetPosition;
    /**
     * Move direction of agent.
     */
    protected Vector3f moveDirection;
    /**
     * Distance of targetPosition that is acceptable.
     */
    protected float distanceError;

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
            rotateAgent(tpf);
        }
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
    
    /**
     * Method for rotating agent in direction of velocity of agent.
     *
     * @param tpf time per frame
     */
    public void rotateAgent(float tpf) {
        Quaternion q = new Quaternion();
        q.lookAt(moveDirection, new Vector3f(0, 1, 0));
        agent.getLocalRotation().slerp(q, agent.getRotationSpeed() * tpf);
    }
}
