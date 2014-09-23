//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. 
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Brent Owens: "Evasion is analogous to pursuit, except that flee is used to
 * steer away from the predicted future position of the target character."
 *
 * @author Jesús Martín Berlanga
 * @version 1.2
 */
public class EvadeBehaviour extends FleeBehaviour {

    /**
     * @throws EvadeWithoutTargetException If target is null
     * @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent)
     */
    public EvadeBehaviour(Agent agent, Agent target) {
        super(agent, target);
        this.validateTarget(target);
    }

    /**
     * @throws EvadeWithoutTargetException If target is null
     * @see FleeBehaviour#FleeBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)
     */
    public EvadeBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, target, spatial);
        this.validateTarget(target);
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class EvadeWithoutTargetException extends IllegalBehaviourException {

        private EvadeWithoutTargetException(String msg) {
            super(msg);
        }
    }

    private void validateTarget(Agent target) {
        if (target == null) {
            throw new EvadeWithoutTargetException("The target can not be null.");
        }
    }

    /**
     * @see FleeBehaviour#calculateRawSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        Vector3f projectedLocation = this.getTarget().getPredictedPosition();

        //Return flee steering force
        Vector3f desiredVelocity = projectedLocation.subtract(agent.getLocalTranslation());
        return desiredVelocity.subtract(velocity).negate();
    }
}
