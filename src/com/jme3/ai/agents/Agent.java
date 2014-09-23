//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. 
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents;

import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.SimpleMainBehaviour;
import com.jme3.ai.agents.util.AbstractWeapon;
import com.jme3.ai.agents.util.GameObject;

import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Class that represents Agent.
 *
 * @author Jesús Martín Berlanga
 * @author Tihomir Radosavljević
 * @version 1.6.0
 */
public final class Agent<T> extends GameObject {

    /**
     * Class that enables you to add all variable you need for your agent.
     */
    private T model;
    /**
     * Unique name of Agent.
     */
    private String name;
    /**
     * Name of team. Primarily used for enabling friendly fire.
     */
    private Team team;
    /**
     * AbstractWeapon used by agent.
     */
    private AbstractWeapon weapon;
    /**
     * Main behaviour of Agent. Behaviour that will be active while his alive.
     */
    private Behaviour mainBehaviour;
    /**
     * Visibility range. How far agent can see.
     */
    private float visibilityRange;
    /**
     * Camera that is attached to agent.
     */
    private Camera camera;

    /**
     * @param name unique name/id of agent
     */
    public Agent(String name) {
        this.name = name;
        velocity = Vector3f.UNIT_XYZ.clone();
    }

    /**
     * @param name unique name/id of agent
     * @param spatial spatial that will agent have durring game
     */
    public Agent(String name, Spatial spatial) {
        this.name = name;
        this.spatial = spatial;
        velocity = Vector3f.UNIT_XYZ.clone();
    }

    /**
     * @return weapon that agent is currently using
     */
    public AbstractWeapon getWeapon() {
        return weapon;
    }

    /**
     * It will add weapon to agent and add its spatial to agent, if there
     * already was weapon before with its own spatial, it will remove it before
     * adding new weapon spatial.
     *
     * @param weapon that agent will use
     */
    public void setWeapon(AbstractWeapon weapon) {
        //remove previous weapon spatial
        if (this.weapon != null && this.weapon.getSpatial() != null) {
            this.weapon.getSpatial().removeFromParent();
        }
        //add new weapon spatial if there is any
        if (weapon.getSpatial() != null) {
            ((Node) spatial).attachChild(weapon.getSpatial());
        }
        this.weapon = weapon;
    }

    /**
     * @return main behaviour of agent
     */
    public Behaviour getMainBehaviour() {
        return mainBehaviour;
    }

    /**
     * Setting main behaviour to agent. For more how should main behaviour look
     * like:
     *
     * @see SimpleMainBehaviour
     * @param mainBehaviour
     */
    public void setMainBehaviour(Behaviour mainBehaviour) {
        this.mainBehaviour = mainBehaviour;
        this.mainBehaviour.setEnabled(false);
    }

    /**
     * @return unique name/id of agent
     */
    public String getName() {
        return name;
    }

    /**
     * Method for starting agent. Note: Agent must be alive to be started.
     *
     * @see Agent#enabled
     */
    public void start() {
        enabled = true;
        mainBehaviour.setEnabled(true);
    }

    /**
     * Method for stoping agent. Note: It will not remove spatial, it will just
     * stop agent from acting.
     *
     * @see Agent#enabled
     */
    public void stop() {
        enabled = false;
        mainBehaviour.setEnabled(false);
    }

    /**
     * @return visibility range of agent
     */
    public float getVisibilityRange() {
        return visibilityRange;
    }

    /**
     * @param visibilityRange how far agent can see
     */
    public void setVisibilityRange(float visibilityRange) {
        this.visibilityRange = visibilityRange;
    }

    /**
     * @return model of agent
     */
    public T getModel() {
        return model;
    }

    /**
     * @param model of agent
     */
    public void setModel(T model) {
        this.model = model;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.model != null ? this.model.hashCode() : 0);
        hash = 47 * hash + (this.team != null ? this.team.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agent<T> other = (Agent<T>) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.team != other.team && (this.team == null || !this.team.equals(other.team))) {
            return false;
        }
        return true;
    }

    @Override
    protected void controlUpdate(float tpf) {

        if (mainBehaviour != null) {
            mainBehaviour.update(tpf);
        }
        //for updating cooldown on weapon
        if (weapon != null) {
            weapon.update(tpf);
        }
        //updateable
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * @return team in which agent belongs
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @param team in which agent belongs
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Check if this agent is in same team as another agent.
     *
     * @param agent
     * @return true if they are in same team, false otherwise
     */
    public boolean isSameTeam(Agent agent) {
        if (team == null || agent.getTeam() == null) {
            return false;
        }
        return team.equals(agent.getTeam());
    }

    /**
     * @return camera that is attached to agent
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Setting camera for agent. It is recommended for use mouse input.
     *
     * @param camera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Check if this agent is considered in the same "neighborhood" in relation
     * with another agent. <br> <br>
     *
     * If the distance is lower than minDistance It is definitely considered in
     * the same neighborhood. <br> <br>
     *
     * If the distance is higher than maxDistance It is defenitely not
     * considered in the same neighborhood. <br> <br>
     *
     * If the distance is inside [minDistance. maxDistance] It is considered in
     * the same neighborhood if the forwardness is higher than "1 -
     * sinMaxAngle".
     *
     * @param Agent The other agent
     * @param minDistance Min. distance to be in the same "neighborhood"
     * @param maxDistance Max. distance to be in the same "neighborhood"
     * @param MaxAngle Max angle in radians
     *
     * @throws InvalidNeighborhoodIDistanceException If minDistance or
     * maxDistance is lower than 0
     *
     * @return If this agent is in the same "neighborhood" in relation with
     * another agent.
     */
    public boolean inBoidNeighborhood(Agent neighbour, float minDistance, float maxDistance, float MaxAngle) {
        if (minDistance < 0) {
            throw new InvalidNeighborhoodIDistanceException("The min distance can not be negative. Current value is " + minDistance);
        } else if (maxDistance < 0) {
            throw new InvalidNeighborhoodIDistanceException("The max distance can not be negative. Current value is " + maxDistance);
        }

        boolean isInBoidNeighborhood;

        if (this == neighbour) {
            isInBoidNeighborhood = false;
        } else {
            float distanceSquared = distanceSquaredRelativeToGameObject(neighbour);

            // definitely in neighborhood if inside minDistance sphere
            if (distanceSquared < (minDistance * minDistance)) {
                isInBoidNeighborhood = true;
            } // definitely not in neighborhood if outside maxDistance sphere
            else if (distanceSquared > maxDistance * maxDistance) {
                isInBoidNeighborhood = false;
            } // otherwise, test angular offset from forward axis.
            else {

                if (this.getAcceleration() != null) {
                    Vector3f unitOffset = this.offset(neighbour).divide(distanceSquared);
                    float forwardness = this.forwardness(unitOffset);
                    isInBoidNeighborhood = forwardness > FastMath.cos(MaxAngle);
                } else {
                    isInBoidNeighborhood = false;
                }
            }
        }

        return isInBoidNeighborhood;
    }

    /**
     * "Given two vehicles, based on their current positions and velocities,
     * determine the time until nearest approach."
     *
     * @param gameObject Other gameObject
     * @return The time until nearest approach
     */
    public float predictNearestApproachTime(GameObject gameObject) {
        Vector3f agentVelocity = velocity;
        Vector3f otherVelocity = gameObject.getVelocity();

        if (agentVelocity == null) {
            agentVelocity = new Vector3f();
        }

        if (otherVelocity == null) {
            otherVelocity = new Vector3f();
        }

        /* "imagine we are at the origin with no velocity,
         compute the relative velocity of the other vehicle" */
        Vector3f relVel = otherVelocity.subtract(agentVelocity);
        float relSpeed = relVel.length();

        /* "Now consider the path of the other vehicle in this relative
         space, a line defined by the relative position and velocity.
         The distance from the origin (our vehicle) to that line is
         the nearest approach." */

        // "Take the unit tangent along the other vehicle's path"
        Vector3f relTangent = relVel.divide(relSpeed);

        /* "find distance from its path to origin (compute offset from
         other to us, find length of projection onto path)" */
        Vector3f offset = gameObject.offset(this);
        float projection = relTangent.dot(offset);

        return projection / relSpeed;
    }

    /**
     * "Given the time until nearest approach (predictNearestApproachTime)
     * determine position of each vehicle at that time, and the distance between
     * them"
     *
     * @param gameObject Other gameObject
     * @param time The time until nearest approach
     * @return The time until nearest approach
     *
     * @see Agent#predictNearestApproachTime(com.jme3.ai.agents.Agent)
     */
    public float computeNearestApproachPositions(Agent gameObject, float time) {
        Vector3f agentVelocity = velocity;
        Vector3f otherVelocity = gameObject.getVelocity();

        if (agentVelocity == null) {
            agentVelocity = new Vector3f();
        }

        if (otherVelocity == null) {
            otherVelocity = new Vector3f();
        }

        Vector3f myTravel = agentVelocity.mult(time);
        Vector3f otherTravel = otherVelocity.mult(time);

        return myTravel.distance(otherTravel);
    }

    /**
     * "Given the time until nearest approach (predictNearestApproachTime)
     * determine position of each vehicle at that time, and the distance between
     * them" <br> <br>
     *
     * Anotates the positions at nearest approach in the given vectors.
     *
     * @param gameObject Other gameObject
     * @param time The time until nearest approach
     * @param ourPositionAtNearestApproach Pointer to a vector, This bector will
     * be changed to our position at nearest approach
     * @param hisPositionAtNearestApproach Pointer to a vector, This bector will
     * be changed to other position at nearest approach
     *
     * @return The time until nearest approach
     *
     * @see Agent#predictNearestApproachTime(com.jme3.ai.agents.Agent)
     */
    public float computeNearestApproachPositions(GameObject gameObject, float time, Vector3f ourPositionAtNearestApproach, Vector3f hisPositionAtNearestApproach) {
        Vector3f agentVelocity = this.getVelocity();
        Vector3f otherVelocity = gameObject.getVelocity();

        if (agentVelocity == null) {
            agentVelocity = new Vector3f();
        }

        if (otherVelocity == null) {
            otherVelocity = new Vector3f();
        }

        Vector3f myTravel = agentVelocity.mult(time);
        Vector3f otherTravel = otherVelocity.mult(time);

        //annotation
        ourPositionAtNearestApproach.set(myTravel);
        hisPositionAtNearestApproach.set(otherTravel);

        return myTravel.distance(otherTravel);
    }

    /**
     *
     * @see IllegalArgumentException
     * @author Jesús Martín Berlanga
     */
    public static class InvalidNeighborhoodIDistanceException extends IllegalArgumentException {

        private InvalidNeighborhoodIDistanceException(String msg) {
            super(msg);
        }
    }
}
