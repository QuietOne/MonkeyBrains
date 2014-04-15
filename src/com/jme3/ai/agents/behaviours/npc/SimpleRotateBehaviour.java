package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * Simple rotate behaviour. If spatial isn't added, then it will rotate all
 * agent spatials. If spatial is added then it will rotate only that spatial.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimpleRotateBehaviour extends Behaviour {

    /**
     * Direction of rotation in whic agent should rotate.
     */
    private Quaternion rotationDirection;
    /**
     * Rotation to which agent should turn.
     */
    private Quaternion rotationTarget;

    /**
     *
     * @param agent to whom behaviour belongs
     */
    public SimpleRotateBehaviour(Agent agent) {
        super(agent);
    }

    /**
     *
     * @param agent to whom behaviour belongs
     * @param spatial that will rotate
     */
    public SimpleRotateBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
    }

    @Override
    protected void controlUpdate(float tpf) {
        //if there isn't spatial
        if (spatial == null) {
            //if there is rotation target
            if (rotationTarget != null) {
                rotationDirection = agent.getLocalRotation().clone();
                rotationDirection.slerp(rotationTarget, agent.getRotationSpeed() * tpf);
                agent.getSpatial().rotate(rotationDirection);
            } else {
                //if there is movement direction in which agent should move
                if (rotationDirection != null) {
                    agent.getSpatial().rotate(rotationDirection.mult(agent.getRotationSpeed() * tpf));
                }
            }
        } else {
            //if there is spatial
            //if there is rotation target
            if (rotationTarget != null) {
                rotationDirection = spatial.getLocalRotation().clone();
                rotationDirection.slerp(rotationTarget, agent.getRotationSpeed() * tpf);
                spatial.rotate(rotationDirection);
            } else {
                //if there is movement direction in which agent should move
                if (rotationDirection != null) {
                    spatial.rotate(rotationDirection.mult(agent.getRotationSpeed() * tpf));
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     *
     * @return direction of rotation
     */
    public Quaternion getRotationDirection() {
        return rotationDirection;
    }

    /**
     *
     * @param rotationDirection direction of rotation
     */
    public void setRotationDirection(Quaternion rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    /**
     *
     * @return rotation of target
     */
    public Quaternion getRotationTarget() {
        return rotationTarget;
    }

    /**
     *
     * @param rotationTarget rotation of target
     */
    public void setRotationTarget(Quaternion rotationTarget) {
        this.rotationTarget = rotationTarget;
    }
}