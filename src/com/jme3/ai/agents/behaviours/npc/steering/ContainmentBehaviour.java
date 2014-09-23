//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.IllegalBehaviourException;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * "Containment refers to motion which is restricted to remain within a certain
 * region." <br><br>
 *
 * "To implement: first predict our character's future position, if it is inside
 * the allowed region no corrective steering is necessary. Otherwise we steer
 * towards the allowed region." <br><br>
 *
 * "Examples of containment include: fish swimming in an aquarium and hockey
 * players skating within an ice rink."
 *
 * @author Jesús Martín Berlanga
 * @version 1.0.1
 */
public class ContainmentBehaviour extends AbstractStrengthSteeringBehaviour {

    private Node containmentArea;
    private Vector3f lastExitSurfaceNormal; //Remember the last normal vector from the containment surface
    /*
     * Saves the Normal vector from the triangle where the agent will exit at "surfaceNormal".
     * 
     * Also saves the exit point at "exitPoint"
     */
    private Vector3f exitPoint;
    private Vector3f surfaceNormal;

    /**
     * @param containmentArea Area where the agent will be restricted
     *
     * @throws ContainmentWithoutContainmentAreaException If containmentArea is
     * null
     *
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public ContainmentBehaviour(Agent agent, Node containmentArea) {
        super(agent);
        this.validateContainmentArea(containmentArea);
        this.containmentArea = containmentArea;
    }

    /**
     * @param containmentArea Area where the agent will be restricted
     * @see
     * AbstractStrengthSteeringBehaviour#AbstractStrengthSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public ContainmentBehaviour(Agent agent, Node containmentArea, Spatial spatial) {
        super(agent, spatial);
        this.validateContainmentArea(containmentArea);
        this.containmentArea = containmentArea;
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ContainmentWithInvalidContainmenetAreaException extends IllegalBehaviourException {

        private ContainmentWithInvalidContainmenetAreaException(String msg) {
            super(msg);
        }
    }

    /**
     * @see IllegalBehaviourException
     */
    public static class ContainmentWithoutContainmentAreaException extends IllegalBehaviourException {

        private ContainmentWithoutContainmentAreaException(String msg) {
            super(msg);
        }
    }

    private void validateContainmentArea(Node containmentArea) {
        if (containmentArea == null) {
            throw new ContainmentWithoutContainmentAreaException("The containment area can not be null.");
        } else if (containmentArea.getWorldBound() == null) {
            throw new ContainmentWithInvalidContainmenetAreaException("The containment area must be bounded.");
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
        Vector3f steer = new Vector3f();
        Vector3f predictedPos = this.agent.getPredictedPosition();

        //Check if the agent is outside the area
        if (!this.containmentArea.getWorldBound().contains(this.agent.getLocalTranslation())) {
            //If we know where is the point he exited, return to the area
            if (lastExitSurfaceNormal != null) {
                steer = this.surfaceNormal.mult(this.exitPoint.distance(predictedPos));
            } else {
                steer = this.containmentArea.getWorldBound().getCenter().subtract(this.agent.getLocalTranslation());
            }
        } //Check if correction is necessary
        else if (!this.containmentArea.getWorldBound().contains(predictedPos)) {
            this.processExitSurface();

            if (exitPoint != null && surfaceNormal != null) {
                /* Check If the normal vector will mantain the agent inside the area, 
                 if not flip it */
                if (this.surfaceNormal.angleBetween(this.agent.getVelocity()) < FastMath.PI / 2) {
                    this.surfaceNormal = this.surfaceNormal.negate();
                }

                steer = this.surfaceNormal.mult(this.exitPoint.distance(predictedPos));
            }
        }

        return steer;
    }

    protected void processExitSurface() {
        this.surfaceNormal = null;
        this.exitPoint = null;

        CollisionResults results = new CollisionResults();

        Vector3f vel = this.agent.getVelocity();
        if (vel == null) {
            vel = new Vector3f();
        }

        Ray ray = new Ray(this.agent.getLocalTranslation(), vel);
        this.containmentArea.collideWith(ray, results);

        CollisionResult closestCollision = results.getClosestCollision();

        if (closestCollision != null) {
            this.surfaceNormal = closestCollision.getContactNormal(); //closestCollision.getTriangle(new Triangle()).getNormal();
            this.exitPoint = closestCollision.getContactPoint();
        }
    }
}