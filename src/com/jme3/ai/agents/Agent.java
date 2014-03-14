package com.jme3.ai.agents;

import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.ai.agents.util.AbstractWeapon;

/**
 * Class that represents Agent. Note: Not recommended for extending. Use generics.
 * @author Tihomir Radosavljević
 */
public class Agent<T> {
    /**
     * Class that enables you to add all variable you need for your agent.
     */
    private T model;
    /**
     * Unique name of Agent.
     */
    private String name;
    /**
     * Spatial contains animation that is active when agent isn't doing any behaviour.
     */
    private Spatial spatial;
    /**
     * Maximum health that Agent can have.
     */
    private float maxHealth = 100f;
    /**
     * Current health status of Agent.
     */
    private float health = 100f;
    /**
     * Move speed of agent.
     */
    private float moveSpeed;
    /**
     * Agent acceleration speed.
     */
    private float acceleration;
    /**
     * Maximum movement speed of agent.
     */
    private float maxMoveSpeed;
    /**
     * Name of team. Primarily used for enabling friendly fire.
     */
    private String teamName;
    /**
     * Status of agent. Agent won't update its status if alive==false.
     */
    private boolean alive = true;
    /**
     * AbstractWeapon used by agent.
     */
    private AbstractWeapon weapon;
    /**
     * Main behaviour of Agent. Behaviour that will be active while his alive.
     */
    private Behaviour mainBehaviour;
    /**
     * Visibility range.
     */
    private float visibilityRange;

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
     * 
     * @return local translation of agent
     */
    public Vector3f getLocalTranslation() {
        return spatial.getLocalTranslation();
    }

    /**
     * 
     * @param position local translation of agent
     */
    public void setLocalTranslation(Vector3f position) {
        this.spatial.setLocalTranslation(position);
    }
    
    /**
     * Setting local translation of agent
     * @param x x translation
     * @param y y translation
     * @param z z translation
     */
    public void setLocalTranslation(float x, float y, float z) {
        this.spatial.setLocalTranslation(x, y, z);
    }

    /**
     * Retrive information is agent alive.
     * @return true - alive, false - dead
     */
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public AbstractWeapon getWeapon() {
        return weapon;
    }

    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }

    public Behaviour getMainBehaviour() {
        return mainBehaviour;
    }

    public void setMainBehaviour(Behaviour mainBehaviour) {
        this.mainBehaviour = mainBehaviour;
        mainBehaviour.setEnabled(false);
    }

    public String getName() {
        return name;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    /**
     * Method for increasing agents health for fixed amount. If adding health
     * will cross the maximum health of agent, then agent's health status will
     * be set to maximum allowed health for that agent.
     * @see Agent#maxHealth
     * @param potionHealth amount of health that should be added
     */
    public void increaseHealth(float potionHealth){
        health += potionHealth;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }
    
    /**
     * Method for decreasing agents health for fixed amount. If health drops to 
     * zero or bellow, agent's health status will be set to zero and he will be
     * dead.
     * @see Agent#alive
     * @param damage amount of health that should be removed
     */
    public void decreaseHealth(double damage) {
        health -= damage;
        if (health<=0) {
            health = 0;
            alive = false;
        }
    }

    public Quaternion getLocalRotation() {
        return spatial.getLocalRotation();
    }

    public void setLocalRotation(Quaternion rotation) {
        spatial.setLocalRotation(rotation);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
    /**
     * Method for starting agent. Note: Agent must be alive to be started.
     * @see Agent#alive
     */
    public void start() {
         mainBehaviour.setEnabled(alive);
     } 

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getVisibilityRange() {
        return visibilityRange;
    }

    public void setVisibilityRange(float visibilityRange) {
        this.visibilityRange = visibilityRange;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
     /**
      * Update method for agent.<br>
      * Note: Agent must be alive and to have mainBehaviour.<br>
      * Warrning: Game.update() will update all agents in game and is not recommended
      * for use.
      * @see Agent#mainBehaviour
      * @see Agent#alive
      * @see util.Game
      * @param tpf time per frame
      */
    public void update(float tpf) {
        if(mainBehaviour!=null && alive)
        mainBehaviour.update(tpf);
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getMaxMoveSpeed() {
        return maxMoveSpeed;
    }

    public void setMaxMoveSpeed(float maxMoveSpeed) {
        this.maxMoveSpeed = maxMoveSpeed;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public boolean isSameTeam(Agent agent) {
        return teamName.equals(agent.getTeamName());
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
        if ((this.teamName == null) ? (other.teamName != null) : !this.teamName.equals(other.teamName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.teamName != null ? this.teamName.hashCode() : 0);
        return hash;
    }
}
