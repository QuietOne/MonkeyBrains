//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. 
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.AgentExceptions;
import com.jme3.ai.agents.behaviours.npc.steering.SteeringExceptions.NegativeMaxDistanceException;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.List;

/**
 * Move toward center of neighbors.
 *
 * @author Jesús Martín Berlanga
 * @version 1.1.2
 */
public class CohesionBehaviour extends AbstractStrengthSteeringBehaviour {

    private List<Agent> neighbours;
    private float maxDistance = Float.POSITIVE_INFINITY;
    private float maxAngle = FastMath.PI / 2;

    /**
     * maxAngle is setted to PI / 2 by default and maxDistance to infinite.
     *
     * @param agent To whom behaviour belongs.
     * @param neighbours Neighbours, this agent is moving toward the center of
     * this neighbours.
     */
    public CohesionBehaviour(Agent agent) {
        super(agent);
        try {
            this.neighbours = agent.getTeam().getMembers();
        } catch (NullPointerException npe) {
            throw new AgentExceptions.TeamNotFoundExecption(agent);
        }

    }

    /**
     * @param maxDistance In order to consider a neihbour inside the
     * neighbourhood
     * @param maxAngle In order to consider a neihbour inside the neighbourhood
     *
     * @throws NegativeMaxDistanceException If maxDistance is lower than 0
     *
     * @see Agent#inBoidNeighborhoodMaxAngle(com.jme3.ai.agents.Agent, float,
     * float, float)
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public CohesionBehaviour(Agent agent, float maxDistance, float maxAngle) {
        super(agent);
        try {
            this.validateMaxDistance(maxDistance);
            this.maxDistance = maxDistance;
            this.maxAngle = maxAngle;
            this.neighbours = agent.getTeam().getMembers();
        } catch (NullPointerException npe) {
            throw new AgentExceptions.TeamNotFoundExecption(agent);
        }
    }

    /**
     * @param spatial active spatial during excecution of behaviour
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public CohesionBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        try {
            this.neighbours = agent.getTeam().getMembers();
        } catch (NullPointerException npe) {
            throw new AgentExceptions.TeamNotFoundExecption(agent);
        }
    }

    /**
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List, float, float)
     */
    public CohesionBehaviour(Agent agent, float maxDistance, float maxAngle, Spatial spatial) {
        super(agent, spatial);
        try {
            this.validateMaxDistance(maxDistance);
            this.maxDistance = maxDistance;
            this.maxAngle = maxAngle;
            this.neighbours = agent.getTeam().getMembers();
        } catch (NullPointerException npe) {
            throw new AgentExceptions.TeamNotFoundExecption(agent);
        }
    }

    /**
     * maxAngle is setted to PI / 2 by default and maxDistance to infinite.
     *
     * @param agent To whom behaviour belongs.
     * @param neighbours Neighbours, this agent is moving toward the center of
     * this neighbours.
     */
    public CohesionBehaviour(Agent agent, List<Agent> neighbours) {
        super(agent);
        this.neighbours = neighbours;
    }

    /**
     * @param maxDistance In order to consider a neihbour inside the
     * neighbourhood
     * @param maxAngle In order to consider a neihbour inside the neighbourhood
     *
     * @throws NegativeMaxDistanceException If maxDistance is lower than 0
     *
     * @see Agent#inBoidNeighborhoodMaxAngle(com.jme3.ai.agents.Agent, float,
     * float, float)
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public CohesionBehaviour(Agent agent, List<Agent> neighbours, float maxDistance, float maxAngle) {
        super(agent);
        this.validateMaxDistance(maxDistance);
        this.neighbours = neighbours;
        this.maxDistance = maxDistance;
        this.maxAngle = maxAngle;
    }

    /**
     * @param spatial active spatial during excecution of behaviour
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public CohesionBehaviour(Agent agent, List<Agent> neighbours, Spatial spatial) {
        super(agent, spatial);
        this.neighbours = neighbours;
    }

    /**
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     * @see CohesionBehaviour#CohesionBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List, float, float)
     */
    public CohesionBehaviour(Agent agent, List<Agent> neighbours, float maxDistance, float maxAngle, Spatial spatial) {
        super(agent, spatial);
        this.validateMaxDistance(maxDistance);
        this.neighbours = neighbours;
        this.maxDistance = maxDistance;
        this.maxAngle = maxAngle;
    }

    private void validateMaxDistance(float maxDistance) {
        if (maxDistance < 0) {
            throw new NegativeMaxDistanceException(maxDistance);
        }
    }

    /**
     * @see AbstractSteeringBehaviour#calculateSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        // steering accumulator and count of neighbors, both initially zero
        Vector3f steering = new Vector3f();
        int realNeighbors = 0;

        // for each of the other vehicles...
        for (Agent agentO : this.neighbours) {
            if (this.agent.inBoidNeighborhood(agentO, this.agent.getRadius() * 3, this.maxDistance, this.maxAngle)) {
                // accumulate sum of neighbor's positions
                steering = steering.add(agentO.getLocalTranslation());
                realNeighbors++;
            }
        }

        // divide by neighbors, subtract off current position to get error-correcting direction
        if (realNeighbors > 0) {
            steering = steering.divide(realNeighbors);
            steering = this.agent.offset(steering);
        }
        return steering;
    }

    public void setNeighbours(List<Agent> neighbours) {
        this.neighbours = neighbours;
    }
}