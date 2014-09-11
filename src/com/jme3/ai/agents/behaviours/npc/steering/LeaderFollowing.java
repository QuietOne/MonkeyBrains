//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * This is similar to pursuit behaviour, but pursuiers must stay away from the
 * pursued path. In order to stay away from that path the pursuers resort to
 * "evade". Furthermore pursuers use "arrive" instead of "seek" to approach to
 * their objective.
 *
 * @see PursuitBehaviour
 *
 * @author Jesús Martín Berlanga
 * @version 1.4
 */
public class LeaderFollowing extends SeekBehaviour {

    private float distanceToChangeFocus;
    private float distanceToEvade;
    private float minimumAngle;
    private ArriveBehaviour arriveBehaviour;
    private EvadeBehaviour evadeBehaviour;

    /**
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent)
     */
    public LeaderFollowing(Agent agent, Agent target) {
        super(agent, target);
        this.evadeBehaviour = new EvadeBehaviour(agent, target);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);

        //Default values
        this.distanceToEvade = 2;
        this.distanceToChangeFocus = 5;
        this.minimumAngle = FastMath.PI / 2.35f;
    }

    /**
     * @see SeekBehaviour#SeekBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)
     */
    public LeaderFollowing(Agent agent, Agent target, Spatial spatial) {
        super(agent, target, spatial);
        this.evadeBehaviour = new EvadeBehaviour(agent, target, spatial);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);

        //Default values
        this.distanceToEvade = 2;
        this.distanceToChangeFocus = 5;
        this.minimumAngle = FastMath.PI / 2.35f;
    }

    /**
     * @param distanceToChangeFocus Distance to change the focus: After the
     * agent distance to the target is lower than this distance, the agent will
     * progressively stop seeking the future position and instead, directly seek
     * the target.
     * @param distanceToEvade If the agent is in front of the target and the
     * distance to him is lower than distanceToEvade, the agent will evade him
     * in order to stay out of his way.
     * @param minimunAngle Minimum angle betwen the target velocity and the
     * vehicle location.
     *
     * @throws LeaderFollowWithoutLeader If target is null
     * @throws negativeDistanceToEvade If distanceToEvade is lower than 0
     * @throws negativeDistanceToChangeFocus If distanceToEvade is lower than 0
     *
     * @see LeaderFollowing#LeaderFollowing(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent)
     */
    public LeaderFollowing(Agent agent, Agent target, float distanceToEvade, float distanceToChangeFocus, float minimunAngle) {
        super(agent, target);
        this.validateTarget(target);
        this.validateDistanceToEvade(distanceToEvade);
        this.validateDistanceToChangeFocus(distanceToChangeFocus);
        this.distanceToEvade = distanceToEvade;
        this.evadeBehaviour = new EvadeBehaviour(agent, target);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);
        this.distanceToChangeFocus = distanceToChangeFocus;
        this.minimumAngle = minimunAngle;
    }

    /**
     * @see LeaderFollowing#LeaderFollowing(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)
     * @see LeaderFollowing#LeaderFollowing(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.Agent, float, float, float)
     */
    public LeaderFollowing(Agent agent, Agent target, float distanceToEvade, float distanceToChangeFocus, float minimunAngle, Spatial spatial) {
        super(agent, target, spatial);
        this.validateTarget(target);
        this.validateDistanceToEvade(distanceToEvade);
        this.validateDistanceToChangeFocus(distanceToChangeFocus);
        this.distanceToEvade = distanceToEvade;
        this.evadeBehaviour = new EvadeBehaviour(agent, target, spatial);
        this.arriveBehaviour = new ArriveBehaviour(agent, target);
        this.distanceToChangeFocus = distanceToChangeFocus;
        this.minimumAngle = minimunAngle;
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class negativeDistanceToEvade extends IllegalBehaviourException {

        private negativeDistanceToEvade(String msg) {
            super(msg);
        }
    }

    private void validateDistanceToEvade(float distanceToEvade) {
        if (distanceToEvade < 0) {
            throw new negativeDistanceToEvade("The distance to evade can not be negative. Current value is " + distanceToEvade);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class LeaderFollowWithoutLeader extends IllegalBehaviourException {

        private LeaderFollowWithoutLeader(String msg) {
            super(msg);
        }
    }

    private void validateTarget(Agent target) {
        if (target == null) {
            throw new LeaderFollowWithoutLeader("The target can not be null.");
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class negativeDistanceToChangeFocus extends IllegalBehaviourException {

        private negativeDistanceToChangeFocus(String msg) {
            super(msg);
        }
    }

    private void validateDistanceToChangeFocus(float distanceToChangeFocus) {
        if (distanceToChangeFocus < 0) {
            throw new negativeDistanceToChangeFocus("The distance to change focus can not be negative. Current value is " + distanceToChangeFocus);
        }
    }

    /**
     * @see AbstractStrengthSteeringBehaviour#calculateFullSteering()
     */
    @Override
    protected Vector3f calculateFullSteering() {
        Vector3f steer;
        float distanceBetwen = this.agent.distanceRelativeToGameObject(this.getTarget());

        //See how far ahead we need to leed
        Vector3f fullProjectedLocation = this.getTarget().getPredictedPosition();
        Vector3f predictedPositionDiff = fullProjectedLocation.subtract(this.getTarget().getLocalTranslation());
        Vector3f projectedLocation = this.getTarget().getLocalTranslation().add(predictedPositionDiff.mult(
                this.calculateFocusFactor(distanceBetwen)));

        this.arriveBehaviour.setSeekingPos(projectedLocation);

        steer = this.arriveBehaviour.calculateFullSteering();

        if (!(distanceBetwen > this.distanceToEvade) && !(this.getTarget().forwardness(this.agent) < FastMath.cos(this.minimumAngle))) { //Incorrect angle and Is in the proper distance to evade -> Evade the leader

            Vector3f arriveSteer = steer.mult(distanceBetwen / this.distanceToEvade);
            Vector3f evadeSteer = this.evadeBehaviour.calculateFullSteering();
            evadeSteer.mult(this.distanceToEvade / (1 + distanceBetwen));
            steer = (new Vector3f()).add(arriveSteer).add(evadeSteer);
        }

        return steer;
    }

    //Calculates the factor in order to change the focus
    private float calculateFocusFactor(float distanceFromFocus) {
        float factor;

        if (distanceFromFocus > this.distanceToChangeFocus) {
            factor = 1;
        } else {
            factor = FastMath.pow((1 + distanceFromFocus / this.distanceToChangeFocus), 2);
        }

        return factor;
    }
}