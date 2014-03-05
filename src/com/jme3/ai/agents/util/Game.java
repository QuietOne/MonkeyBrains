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
 * @version 1.0
 */
public class Game {
    
    private boolean over = false;
    private Node rootNode;
    private InputManager inputManager;
    private List<Agent> agents;
    private List<Bullet> bullets;
    
    private Game() {
        agents = new ArrayList<Agent>();
        bullets = new LinkedList<Bullet>();
    }
    
    public void addAgent(Agent agent){
        agent.setAlive(true);
        agents.add(agent);
        rootNode.attachChild(agent.getSpatial());
    }
    
    public void addAgent(Agent agent, Vector3f position) {
        agent.setLocalTranslation(position);
        agent.setAlive(true);
        agents.add(agent);
        rootNode.attachChild(agent.getSpatial());
    }

    public void addAgent(Agent agent, float x, float y, float z) {
        agent.setLocalTranslation(x, y, z);
        agent.setAlive(true);
        agents.add(agent);
        rootNode.attachChild(agent.getSpatial());
    }

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

    public void disableAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).setAlive(false);
                break;
            }
        }
    }
    
    public void decreaseHealth(Agent agent, double damage) {
        //finding agent and decreasing his healthbar
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).decreaseHealth(damage);
                if (!agents.get(i).isAlive()) {
                    //System.out.println("Agent "+agents.get(i).getName()+" is dead.");
                    agents.get(i).setAlive(false);
                    agents.get(i).getSpatial().removeFromParent();
                } else {
                    //System.out.println("Agent "+agents.get(i).getName()+" has left "+agents.get(i).getHealth()+" health.");
                }
                break;
            }
        }
        //checking if only one agent left if it has, then game over
        int numberOfAliveAgents = 0;
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).isAlive()) {
                numberOfAliveAgents++;
            }
        }
        if (numberOfAliveAgents < 2) {
            over = true;
        }
    }

    public void restart() {
        over = false;
        for (int i = 0; i < agents.size(); i++) {
            if (!agents.get(i).isAlive()) {
                agents.get(i).setAlive(true);
                rootNode.attachChild(agents.get(i).getSpatial());
            }
        }
    }

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
     *
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
    
    public static Game getInstance() {
        return GameHolder.INSTANCE;
    }
    
    private static class GameHolder {

        private static final Game INSTANCE = new Game();
    }
}
