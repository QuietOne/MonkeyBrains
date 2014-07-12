package com.jme3.ai.agents;

import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.behaviours.npc.SimpleMainBehaviour;
import com.jme3.scene.Spatial;
import com.jme3.ai.agents.util.AbstractWeapon;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * Class that represents Agent. Note: Not recommended for extending. Use
 * generics.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class Agent<T> extends GameObject {

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
    }

    /**
     * @param name unique name/id of agent
     * @param spatial spatial that will agent have durring game
     */
    public Agent(String name, Spatial spatial) {
        this.name = name;
        this.spatial = spatial;
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
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("You should override it youself");
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
}