package com.jme3.ai.agents.util.control;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.GameEntity;
import com.jme3.ai.agents.util.GameEntityExceptions;
import com.jme3.ai.agents.util.weapons.AbstractWeapon;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.LinkedList;
import java.util.List;

/**
 * Class with information about agents and consequences of their behaviours in
 * game. It is not necessary to use it but it enables easier game status
 * updates. Contains agents and gameEntities and provides generic ai control.
 *
 * @author Tihomir Radosavljević
 * @version 2.0.0
 */
public class AIAppState extends AbstractAppState {

    /**
     * Status of game with agents.
     */
    private boolean inProgress = false;
    /**
     * Indicator if the agent in same team can damage each other.
     */
    protected boolean friendlyFire = true;
    /**
     * Node for all game geometry representation.
     */
    protected Node rootNode;
    /**
     * Application to which this game is attached.
     */
    protected Application app;
    /**
     * Controls of game.
     */
    protected AIControl aiControl;
    /**
     * List of all agents that are active in game.
     */
    protected List<Agent> agents;
    /**
     * List of all GameEntities in game except for agents.
     */
    protected List<GameEntity> gameEntities;

    protected AIAppState() {
        agents = new LinkedList<Agent>();
        gameEntities = new LinkedList<GameEntity>();
    }

    /**
     * Adding agent to game. It will be automaticly updated when game is
     * updated, and agent's position will be one set into Spatial.
     *
     * @param agent agent which is added to game
     */
    public void addAgent(Agent agent) {
        agents.add(agent);
        if (inProgress) {
            agent.start();
        }
        rootNode.attachChild(agent.getSpatial());
    }

    /**
     * Adding agent to game. It will be automaticly updated when game is
     * updated.
     *
     * @param agent agent which is added to game
     * @param position position where spatial should be added
     */
    public void addAgent(Agent agent, Vector3f position) {
        agent.setLocalTranslation(position);
        agents.add(agent);
        if (inProgress) {
            agent.start();
        }
        rootNode.attachChild(agent.getSpatial());
    }

    /**
     * Adding agent to game. It will be automaticly updated when game is
     * updated.
     *
     * @param agent agent which is added to game
     * @param x X coordinate where spatial should be added
     * @param y Y coordinate where spatial should be added
     * @param z Z coordinate where spatial should be added
     */
    public void addAgent(Agent agent, float x, float y, float z) {
        agent.setLocalTranslation(x, y, z);
        agents.add(agent);
        if (inProgress) {
            agent.start();
        }
        rootNode.attachChild(agent.getSpatial());
    }

    /**
     * Removing agent from list of agents to be updated and its spatial from
     * game.
     *
     * @param agent agent who should be removed
     */
    public void removeAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).stop();
                agents.get(i).getSpatial().removeFromParent();
                agents.remove(i);
                break;
            }
        }
    }

    /**
     * Disabling agent. It means from agent will be dead and won't updated.
     *
     * @param agent
     */
    public void disableAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).stop();
                break;
            }
        }
    }

    /**
     * Decreasing hitPoints of agent for value of damage.
     *
     * @param agent
     * @param damage
     */
    public void decreaseHP(Agent agent, double damage) {
        //finding agent and decreasing his healthbar
        int i = 0;
        try {
            for (; i < agents.size(); i++) {
                if (agents.get(i).equals(agent)) {
                    agents.get(i).getHpSystem().decreaseHP(damage);
                    if (!agents.get(i).isEnabled()) {
                        agents.get(i).stop();
                        agents.get(i).getSpatial().removeFromParent();
                    }
                    break;
                }
            }
        } catch (NullPointerException npe) {
            throw new GameEntityExceptions.HPSystemNotFoundException(agent);
        }
    }

    /**
     * Check what agents are seen by one agent.
     *
     * @param agent agent which is looking
     * @param viewAngle
     * @return all agents that is seen by agent
     */
    public List<GameEntity> look(Agent agent, float viewAngle) {
        List<GameEntity> temp = new LinkedList<GameEntity>();
        //are there seen agents
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).isEnabled()) {
                if (!agents.get(i).equals(agent) && lookable(agent, agents.get(i), viewAngle)) {
                    temp.add(agents.get(i));
                }
            }
        }
        for (GameEntity gameEntity : gameEntities) {
            if (gameEntity.isEnabled() && lookable(agent, gameEntity, viewAngle)) {
                temp.add(gameEntity);
            }
        }
        return temp;
    }

    /**
     * Use with cautious. It works for this example, but it is not general it
     * doesn't include obstacles into calculation.
     *
     * @param observer
     * @param gameEntity
     * @param heightAngle
     * @param widthAngle
     * @return
     */
    public boolean lookable(Agent observer, GameEntity gameEntity, float viewAngle) {
        //if agent is not in visible range
        if (observer.getLocalTranslation().distance(gameEntity.getLocalTranslation())
                > observer.getVisibilityRange()) {
            return false;
        }
        Vector3f direction = observer.getLocalRotation().mult(new Vector3f(0, 0, -1));
        Vector3f direction2 = observer.getLocalTranslation().subtract(gameEntity.getLocalTranslation()).normalizeLocal();
        float angle = direction.angleBetween(direction2);
        if (angle > viewAngle) {
            return false;
        }
        return true;
    }

    /**
     * Method that will update all alive agents and fired bullets while active.
     */
    @Override
    public void update(float tpf) {
        if (!inProgress) {
            return;
        }
        for (int i = 0; i < agents.size(); i++) {
            agents.get(i).update(tpf);
        }
        for (int i = 0; i < gameEntities.size(); i++) {
            gameEntities.get(i).update(tpf);
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<GameEntity> getGameEntities() {
        return gameEntities;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    /**
     * Check friendly fire and decreaseHP of target if conditions are ok.
     *
     * @see AIAppState#friendlyFire
     * @see AIAppState#decreaseHP(com.jme3.ai.agents.Agent, double)
     * @param attacker agent who attacks
     * @param target agent who is attacked
     * @param weapon weapon with which is target being attacked
     */
    public void agentAttack(Agent attacker, Agent target, AbstractWeapon weapon) {
        if (friendlyFire && attacker.isSameTeam(target)) {
            return;
        }
        decreaseHP(target, weapon.getAttackDamage());
    }

    public void addGameEntity(GameEntity gameEntity) {
        gameEntities.add(gameEntity);
    }

    public void removeGameEntity(GameEntity gameEntity) {
        gameEntity.getSpatial().removeFromParent();
        gameEntities.remove(gameEntity);
    }

    public static AIAppState getInstance() {
        return GameHolder.INSTANCE;
    }

    public AIControl getAIControl() {
        return aiControl;
    }

    public void setAIControl(AIControl aiControl) {
        this.aiControl = aiControl;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    private static class GameHolder {

        private static final AIAppState INSTANCE = new AIAppState();
    }

    public void start() {
        inProgress = true;
        for (Agent agent : agents) {
            agent.start();
        }
    }

    public void stop() {
        inProgress = false;
        for (Agent agent : agents) {
            agent.stop();
        }
    }

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
        rootNode = (Node) app.getViewPort().getScenes().get(0);
    }
}
