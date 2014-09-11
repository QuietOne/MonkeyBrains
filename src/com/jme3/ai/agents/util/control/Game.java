package com.jme3.ai.agents.util.control;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.AbstractWeapon;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.LinkedList;
import java.util.List;

/**
 * Class with information about agents and consequences of their behaviours in
 * game. It is not necessary to use it but it enables easier game status
 * updates. Contains agents and gameObjects and provides generic game control.
 *
 * @author Tihomir Radosavljević
 * @version 1.2
 */
public class Game extends AbstractAppState{

    /**
     * Status of game with agents.
     */
    protected boolean over = false;
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
    protected GameControl gameControl;
    /**
     * List of all agents that are active in game.
     */
    protected List<Agent> agents;
    /**
     * List of all GameObjects in game except for agents.
     */
    protected List<GameObject> gameObjects;

    protected Game() {
        agents = new LinkedList<Agent>();
        gameObjects = new LinkedList<GameObject>();
    }

    /**
     * Adding agent to game. It will be automaticly updated when game is
     * updated, and agent's position will be one set into Spatial.
     *
     * @param agent agent which is added to game
     */
    public void addAgent(Agent agent) {
        agent.setEnabled(true);
        agents.add(agent);
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
        agent.setEnabled(true);
        agents.add(agent);
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
        agent.setEnabled(true);
        agents.add(agent);
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
                agents.get(i).setEnabled(false);
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
                agents.get(i).setEnabled(false);
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
    public void decreaseHitPoints(Agent agent, double damage) {
        //finding agent and decreasing his healthbar
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).decreaseHitPoints(damage);
                if (!agents.get(i).isEnabled()) {
                    agents.get(i).setEnabled(false);
                    agents.get(i).getSpatial().removeFromParent();
                }
                break;
            }
        }

    }

    /**
     * Check what agents are seen by one agent.
     *
     * @param agent agent which is looking
     * @param viewAngle
     * @return all agents that is seen by agent
     */
    public List<GameObject> look(Agent agent, float viewAngle) {
        List<GameObject> temp = new LinkedList<GameObject>();
        //are there seen agents
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).isEnabled()) {
                if (!agents.get(i).equals(agent) && lookable(agent, agents.get(i), viewAngle)) {
                    temp.add(agents.get(i));
                }
            }
        }
        for (GameObject gameObject : gameObjects) {
            if (gameObject.isEnabled() && lookable(agent, gameObject, viewAngle)) {
                temp.add(gameObject);
            }
        }
        return temp;
    }

    /**
     * Use with cautious. It works for this example, but it is not general it
     * doesn't include obstacles into calculation.
     *
     * @param observer
     * @param gameObject
     * @param heightAngle
     * @param widthAngle
     * @return
     */
    public boolean lookable(Agent observer, GameObject gameObject, float viewAngle) {
        //if agent is not in visible range
        if (observer.getLocalTranslation().distance(gameObject.getLocalTranslation())
                > observer.getVisibilityRange()) {
            return false;
        }
        Vector3f direction = observer.getLocalRotation().mult(new Vector3f(0, 0, -1));
        Vector3f direction2 = observer.getLocalTranslation().subtract(gameObject.getLocalTranslation()).normalizeLocal();
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
        for (int i = 0; i < agents.size(); i++) {
            agents.get(i).update(tpf);
        }
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(tpf);
        }
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    /**
     * Check friendly fire and decreaseHitPoints of target if conditions are ok.
     *
     * @see Game#friendlyFire
     * @see Game#decreaseHitPoints(com.jme3.ai.agents.Agent, double)
     * @param attacker agent who attacks
     * @param target agent who is attacked
     */
    public void agentAttack(Agent attacker, Agent target) {
        if (friendlyFire && attacker.isSameTeam(target)) {
            return;
        }
        decreaseHitPoints(target, attacker.getWeapon().getAttackDamage());
    }
    /**
     * Check friendly fire and decreaseHitPoints of target if conditions are ok.
     * 
     * @see Game#friendlyFire
     * @see Game#decreaseHitPoints(com.jme3.ai.agents.Agent, double)
     * @param attacker agent who attacks
     * @param target agent who is attacked
     * @param weapon weapon with which is target being attacked
     */
    public void agentAttack(Agent attacker, Agent target, AbstractWeapon weapon) {
        if (friendlyFire && attacker.isSameTeam(target)) {
            return;
        }
        decreaseHitPoints(target, weapon.getAttackDamage());
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObject.getSpatial().removeFromParent();
        gameObjects.remove(gameObject);
    }

    public static Game getInstance() {
        return GameHolder.INSTANCE;
    }

    public GameControl getGameControl() {
        return gameControl;
    }

    public void setGameControl(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    private static class GameHolder {

        private static final Game INSTANCE = new Game();
    }

    public void start() {
        for (Agent agent : agents) {
            agent.start();
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
