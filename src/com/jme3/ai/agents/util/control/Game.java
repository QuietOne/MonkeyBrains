package com.jme3.ai.agents.util;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.player.PlayerMainBehaviour;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class with information about agents and consequences of their behaviours in game.
 * @author Tihomir Radosavljević
 */
public class Game {
    /**
     * Status of game with agents.
     */
    private boolean over = false;
    /**
     * Indicator if the agent in same team can damage each other.
     */
    private boolean friendlyFire = false;
    /**
     * Node for all gama geometry representation.
     */
    private Node rootNode;
    /**
     * Managing input for player.
     */
    private InputManager inputManager;
    /**
     * List of all agents that are active in game.
     */
    private List<Agent> agents;
    /**
     * List of all fired bullets in game.
     */
    private List<Bullet> bullets;
    
    private Game() {
        agents = new ArrayList<Agent>();
        bullets = new LinkedList<Bullet>();
    }
    /**
     * Adding agent to game. It will be automaticly updated when game is updated, 
     * and agent's position will be one set into Spatial.
     * @param agent agent which is added to game
     */
    public void addAgent(Agent agent){
        agent.setAlive(true);
        agents.add(agent);
        rootNode.attachChild(agent.getSpatial());
    }
    /**
     * Adding agent to game. It will be automaticly updated when game is updated.
     * @param agent agent which is added to game
     * @param position position where spatial should be added
     */
    public void addAgent(Agent agent, Vector3f position) {
        agent.setLocalTranslation(position);
        agent.setAlive(true);
        agents.add(agent);
        rootNode.attachChild(agent.getSpatial());
    }
    /**
     * Adding agent to game. It will be automaticly updated when game is updated.
     * @param agent agent which is added to game
     * @param x X coordinate where spatial should be added
     * @param y Y coordinate where spatial should be added
     * @param z Z coordinate where spatial should be added
     */
    public void addAgent(Agent agent, float x, float y, float z) {
        agent.setLocalTranslation(x, y, z);
        agent.setAlive(true);
        agents.add(agent);
        rootNode.attachChild(agent.getSpatial());
    }
    /**
     * Removing agent from list of agents to be updated and its spatial from game.
     * @param agent agent who shoud be removed
     */
    public void removeAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).setAlive(false);
                agents.get(i).getSpatial().removeFromParent();
                agents.remove(i);
                break;
            }
        }
    }
    /**
     * Disabling agent. It means from agent will be dead and won't updated.
     * @param agent 
     */
    public void disableAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).setAlive(false);
                break;
            }
        }
    }
    /**
     * Decreasing health of agent for value of damage.
     * @param agent 
     * @param damage 
     */
    public void decreaseHealth(Agent agent, double damage) {
        //finding agent and decreasing his healthbar
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).decreaseHealth(damage);
                if (!agents.get(i).isAlive()) {
                    agents.get(i).setAlive(false);
                    agents.get(i).getSpatial().removeFromParent();
                }
                break;
            }
        }
        //checking if one team left if it has, then game over
        int i = 0;
        //find first alive agent in list
        while (i < agents.size() && !agents.get(i).isAlive()) {
            i++;
        }
        int j = i + 1;
        while (j < agents.size()) {
            //finding next agent that is alive
            if (!agents.get(j).isAlive()) {
                j++;
                continue;
            }
            //if agent is not in the same team, game is certainly not over
            if (!agents.get(i).isSameTeam(agents.get(j))) {
                break;
            }
            j++;
        }
        if (j>=agents.size()) {
            over = true;
        }
    }
    /**
     * Restarting game. All paramethers set to initial values.
     */
    public void restart() {
        over = false;
        for (int i = 0; i < agents.size(); i++) {
            if (!agents.get(i).isAlive()) {
                agents.get(i).setAlive(true);
                rootNode.attachChild(agents.get(i).getSpatial());
            }
        }
    }
    /**
     * Check what agents are seen by one agent.
     * @param agent agent which is looking
     * @param viewAngle 
     * @return all agents that is seen by agent
     */
    public List<Agent> viewPort(Agent agent, float viewAngle) {
        List<Agent> temp = new LinkedList<Agent>();
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).isAlive()) {
                if (!agents.get(i).equals(agent) && lookable(agent, agents.get(i), viewAngle)) {
                    temp.add(agents.get(i));
                }
            }
        }
        return temp;
    }
    /**
     * Use with cautious. It works for this example, but it is not generic.
     * @param observer
     * @param agent
     * @param heightAngle
     * @param widthAngle
     * @return
     */
    public boolean lookable(Agent observer, Agent agent, float viewAngle) {
        //if agent is not in visible range
        if (observer.getLocalTranslation().distance(agent.getLocalTranslation())
                >observer.getVisibilityRange()) {
            return false;
        }
        Vector3f direction = observer.getLocalRotation().mult(new Vector3f(0,0,-1));
        Vector3f direction2 = observer.getLocalTranslation().subtract(agent.getLocalTranslation()).normalizeLocal();
        float angle = direction.angleBetween(direction2);
        if (angle > viewAngle) {
            return false;
        }
        return true;
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void removeBullet(Bullet bullet) {
        bullet.getSpatial().removeFromParent();
        bullets.remove(bullet);
    }

    /**
     * Method that will update all alive agents and fired bullets while active.
     */
    public void update(float tpf) {
        for (Agent agent : agents) {
            agent.update(tpf);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(tpf);

        }
    }
    /**
     * Function for registering input. It is presumed that playable character is
     * first in list. If he isn't, than override this.
     */
    public void registerInput() {
        inputManager.addMapping("moveForward", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(((PlayerMainBehaviour) agents.get(0).getMainBehaviour()).getMoveBehaviour(), "moveForward", "moveBackward", "moveRight", "moveLeft");
        inputManager.addListener(((PlayerMainBehaviour) agents.get(0).getMainBehaviour()).getAttackBehaviour(), "Shoot");
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

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }
    /**
     * Check friendly fire and decreaseHealth of target if conditions are ok.
     * @see Game#friendlyFire
     * @see Game#decreaseHealth(com.jme3.ai.agents.Agent, double)
     * @param attacker agent who attacks
     * @param target agent who is attacked
     */
    public void agentAttack(Agent attacker, Agent target) {
        if (friendlyFire && attacker.isSameTeam(target)) {
            return;
        }
        decreaseHealth(target, attacker.getWeapon().getAttackDamage());
    }
    
    public static Game getInstance() {
        return GameHolder.INSTANCE;
    }
    
    private static class GameHolder {
        private static final Game INSTANCE = new Game();
    }
}
