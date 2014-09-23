//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

/**
 * "The queuing results from a steering behavior which produces braking
 * (deceleration) when the vehicle detects other vehicles which are: nearby, in
 * front of, and moving slower than itself." <br> <br>
 *
 * In most cases you will combine this behaviour with seek, separation and/or
 * avoid. Queuing is designed to be combined with the behaviours mentioned
 * before in order to to leave a large 'room' through a narrow 'doorway': "drawn
 * toward the 'doorway' by seek behavior, avoid walls, and maintain separation
 * from each other."
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.1
 */
public class QueuingBehaviour extends AbstractStrengthSteeringBehaviour {

    private List<Agent> neighbours;
    private float minDistance;

    /**
     * @param neighbours Queue of agents
     * @param minDistance Min. distance from center to center to consider a
     * neighbour as an obstacle
     *
     * @throws QueuingWithNegativeMinDistanceException If minDistance is lower than 0
     *
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public QueuingBehaviour(Agent agent, List<Agent> neighbours, float minDistance) {
        super(agent);
        this.validateMinDistance(minDistance);
        this.neighbours = neighbours;
        this.minDistance = minDistance;
    }

    /**
     * @see QueuingBehaviour#QueuingBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List, float)
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public QueuingBehaviour(Agent agent, List<Agent> neighbours, float minDistance, Spatial spatial) {
        super(agent, spatial);
        this.validateMinDistance(minDistance);
        this.neighbours = neighbours;
        this.minDistance = minDistance;
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class QueuingWithNegativeMinDistanceException extends IllegalBehaviourException {

        private QueuingWithNegativeMinDistanceException(String msg) {
            super(msg);
        }
    }

    private void validateMinDistance(float minDistance) {
        if (minDistance < 0) {
            throw new QueuingWithNegativeMinDistanceException("The min distance from an obstacle can not be negative. Current value is " + minDistance);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * @see AbstractStrengthSteeringBehaviour#calculateRawSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        Vector3f agentVelocity = this.agent.getVelocity();

        int numberObstaclesFactor = 1;
        float distanceFactor = 1;
        float velocityFactor = 1;

        if (agentVelocity != null && !agentVelocity.equals(Vector3f.ZERO)) {
            for (Agent neighbour : this.neighbours) {
                Vector3f neighVel = neighbour.getVelocity();
                float fordwardness = this.agent.forwardness(neighbour);
                float velDiff;
                float distance;

                if (neighbour != this.agent
                        && (distance = this.agent.distanceRelativeToGameObject(neighbour)) < this.minDistance
                        && fordwardness > 0
                        && neighVel != null
                        && (velDiff = neighVel.length() - agentVelocity.length()) < 0) {
                    distanceFactor *= distance / this.minDistance;
                    velocityFactor *= -velDiff / this.agent.getMoveSpeed();
                    numberObstaclesFactor++;
                }
            }
        }

        this.setBrakingFactor((distanceFactor + velocityFactor + (1 / numberObstaclesFactor)) / 3);

        return new Vector3f();
    }

    public void setNeighbours(List<Agent> neighbours) {
        this.neighbours = neighbours;
    }
}