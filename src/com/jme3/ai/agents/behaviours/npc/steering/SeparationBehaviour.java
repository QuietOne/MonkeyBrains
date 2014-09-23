//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.math.Vector3f;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

/**
 * Brent Owens: "Separation steering behavior gives a character the ability to
 * maintain a certain separation distance from others nearby. This can be used
 * to prevent characters from crowding together. <br><br>
 *
 * For each nearby character, a repulsive force is computed by subtracting the
 * positions of our character and the nearby character, normalizing, and then
 * applying a 1/r weighting. (That is, the position offset vector is scaled by
 * 1/r^2.)". "These repulsive forces for each nearby character are summed
 * together to produce the overall steering force. <br><br>
 *
 * The supplied neighbours should only be the nearby neighbours in the field of
 * view of the character that is steering. It is good to ignore anything behind
 * the character."
 *
 * @author Jesús Martín Berlanga
 * @version 1.3.2
 */
public class SeparationBehaviour extends AbstractStrengthSteeringBehaviour {

    private float minDistance;
    /*
     * List of the obstacles that we want to be separated
     */
    private List<Agent> obstacles;

    /**
     * @param agent To whom behaviour belongs.
     * @param initialObstacles Initializes a list with the obstacles from the
     * agent want to be separated
     */
    public SeparationBehaviour(Agent agent, List<Agent> initialObstacles) {
        super(agent);
        this.obstacles = initialObstacles;
        this.minDistance = Float.POSITIVE_INFINITY;
    }

    /**
     * @param spatial active spatial during excecution of behaviour
     * @see SeparationBehaviour#SeparationBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public SeparationBehaviour(Agent agent, List<Agent> initialObstacles, Spatial spatial) {
        super(agent, spatial);
        this.obstacles = initialObstacles;
        this.minDistance = Float.POSITIVE_INFINITY;
    }

    /**
     * @param minDistance Min. distance from center to center to consider an
     * obstacle
     * @see SeparationBehaviour#SeparationBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List)
     */
    public SeparationBehaviour(Agent agent, List<Agent> initialObstacles, float minDistance) {
        super(agent);
        this.validateMinDistance(minDistance);
        this.obstacles = initialObstacles;
        this.minDistance = minDistance;
    }

    /**
     * @param spatial active spatial during excecution of behaviour
     * @see SeparationBehaviour#SeparationBehaviour(com.jme3.ai.agents.Agent,
     * java.util.List, float)
     */
    public SeparationBehaviour(Agent agent, List<Agent> initialObstacles, float minDistance, Spatial spatial) {
        super(agent, spatial);
        this.validateMinDistance(minDistance);
        this.obstacles = initialObstacles;
        this.minDistance = minDistance;
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ObstacleAvoindanceWithNegativeMinDistanceException extends IllegalBehaviourException {

        private ObstacleAvoindanceWithNegativeMinDistanceException(String msg) {
            super(msg);
        }
    }

    private void validateMinDistance(float minDistance) {
        if (minDistance < 0) {
            throw new ObstacleAvoindanceWithNegativeMinDistanceException("The min distance from an obstacle can not be negative. Current value is " + minDistance);
        }
    }

    /**
     * @see AbstractSteeringBehaviour#calculateSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {

        //Propities whom behaviour belongs.
        Vector3f agentLocation = super.agent.getLocalTranslation();

        Vector3f steering = new Vector3f();

        for (Agent oAgent : this.obstacles) {
            if (oAgent != this.agent && oAgent.distanceRelativeToGameObject(this.agent) < this.minDistance) //If the obstacle is not himself
            {
                Vector3f loc = oAgent.getLocalTranslation().subtract(agentLocation);
                float len2 = loc.lengthSquared();
                loc.normalizeLocal();
                steering.addLocal(loc.negate().mult(1f / ((float) FastMath.pow(len2, 2))));
            }
        }

        return steering;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public void setObstacles(List<Agent> obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
