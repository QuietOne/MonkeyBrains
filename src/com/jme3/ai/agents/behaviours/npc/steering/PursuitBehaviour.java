//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Brent Owens: "Pursuit is similar to seek except that the quarry (target) is
 * another moving character. Effective pursuit requires a prediction of the
 * target’s future position."
 *
 * @author Jesús Martín Berlanga
 * @version 1.2
 */
public class PursuitBehaviour extends SeekBehaviour {

    /**
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent)
     * @throws PursuitWithoutTarget If target is null
     */
    public PursuitBehaviour(Agent agent, Agent target) {
        super(agent, target);
        this.validateTarget(target);
    }

    /**
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)
     * @throws PursuitWithoutTarget If target is null
     */
    public PursuitBehaviour(Agent agent, Agent target, Spatial spatial) {
        super(agent, target, spatial);
        this.validateTarget(target);
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class PursuitWithoutTarget extends IllegalBehaviourException {

        private PursuitWithoutTarget(String msg) {
            super(msg);
        }
    }

    private void validateTarget(Agent target) {
        if (target == null) {
            throw new PursuitWithoutTarget("The target can not be null.");
        }
    }

    /**
     * @see AbstractStrengthSteeringBehaviour#calculateFullSteering()
     */
    @Override
    protected Vector3f calculateFullSteering() {
        //See how far ahead we need to leed
        Vector3f projectedLocation = this.getTarget().getPredictedPosition();

        //Seek behaviour
        Vector3f desierdVel = projectedLocation.subtract(this.agent.getLocalTranslation());

        Vector3f aVelocity = this.agent.getVelocity();

        if (aVelocity == null) {
            aVelocity = new Vector3f();
        }

        return desierdVel.subtract(aVelocity);
    }
}
