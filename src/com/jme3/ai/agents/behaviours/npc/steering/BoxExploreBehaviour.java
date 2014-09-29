//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 * "Explore goal is to exhaustively cover a region of space". <br><br>
 *
 * This is the simplest implementation of explore behaviour.
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.1
 */
public class BoxExploreBehaviour extends AbstractStrengthSteeringBehaviour {

    private Vector3f zeroCorner;
    private float boxWidthX;
    private float boxWidthZ;
    private float boxHeight;
    private float subdivisionDistance;
    private boolean isFinished = false;
    private List<Vector3f> targets = new ArrayList<Vector3f>();

    /**
     * @param boxCenter Center position of the box
     * @param boxWidthX Box x size
     * @param boxWidthZ Box z size
     * @param boxHeight Box y size
     * @param subdivisionDistance Distance between each subdivision. A lower
     * subdivision distance means that the agent will explore the region more
     * exhaustively.
     *
     * @throws BoxExploreWithNegativeSubdivisionDistanceException If
     * subdivisionDistance is lower or equals to 0
     * @throws BoxExploreWithNegativeBoxDimensionsException If boxWidthX,
     * boxWidthZ or boxHeight is lower than 0
     *
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public BoxExploreBehaviour(Agent agent, Vector3f boxCenter, float boxWidthX, float boxWidthZ, float boxHeight, float subdivisionDistance) {
        super(agent);
        this.construct(boxCenter, boxWidthX, boxWidthZ, boxHeight, subdivisionDistance);
    }

    /**
     * @see BoxExploreBehaviour#BoxExploreBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.math.Vector3f, float, float, float, float)
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public BoxExploreBehaviour(Agent agent, Vector3f boxCenter, float boxWidthX, float boxWidthZ, float boxHeight, float subdivisionDistance, Spatial spatial) {
        super(agent, spatial);
        this.construct(boxCenter, boxWidthX, boxWidthZ, boxHeight, subdivisionDistance);
    }

    private void construct(Vector3f boxCenter, float boxWidthX, float boxWidthZ, float boxHeight, float subdivisionDistance) {
        if (boxWidthX < 0 || boxWidthZ < 0 || boxHeight < 0) {
            throw new SteeringExceptions.BoxExploreWithNegativeValueException("Box width, depth and height must be positive.");
        } else if (subdivisionDistance <= 0) {
            throw new SteeringExceptions.BoxExploreWithNegativeValueException("The subdivision distance must be higher than 0.");
        }

        this.boxWidthX = boxWidthX;
        this.boxWidthZ = boxWidthZ;
        this.boxHeight = boxHeight;
        this.subdivisionDistance = subdivisionDistance;
        this.zeroCorner = boxCenter.subtract(new Vector3f(this.boxHeight / 2, this.boxHeight / 2, this.boxWidthZ / 2));
        this.addNewTargets();
    }

    /**
     * Should be used after cleaning target list
     */
    protected void addNewTargets() {
        //Vertical subdivisions
        //1st Horizontal subdivisions in each vertical subdivision
        for (float i = 0; i < this.boxHeight; i += this.subdivisionDistance) {
            //2nd Horizontal subdivisions
            for (float j = 0; j < this.boxWidthX; j += this.subdivisionDistance) {
                for (float k = 0; k < this.boxWidthZ; k += this.subdivisionDistance) {
                    this.targets.add(this.zeroCorner.add(new Vector3f(j, i, k)));
                }
            }
        }
    }

    /**
     * @see AbstractSteeringBehaviour#calculateSteering()
     */
    @Override
    protected Vector3f calculateRawSteering() {
        Vector3f steer = Vector3f.ZERO;

        if (!isFinished) {
            Vector3f closest = null;
            float closestDistance = Float.POSITIVE_INFINITY;

            for (int i = 0; i < this.targets.size(); i++) {
                Vector3f target = this.targets.get(i);
                float distanceFromTarget = this.agent.offset(target).length();

                if (distanceFromTarget < this.subdivisionDistance / 2) {
                    this.targets.remove(i);
                } else if (distanceFromTarget < closestDistance) {
                    closest = target;
                    closestDistance = distanceFromTarget;
                }
            }

            if (closest != null) {
                SeekBehaviour seek = new SeekBehaviour(this.agent, closest);
                steer = seek.calculateRawSteering();
            } else {
                isFinished = true;
            }
        }
        return steer;
    }

    public boolean getIsFinished() {
        return this.isFinished;
    }

    /**
     * The agent will have to explore the region again and all the progress will
     * be lost.
     */
    protected void resetExplore() {
        this.targets.clear();
        this.addNewTargets();
        this.isFinished = false;
    }
}