package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.ai.agents.events.GameObjectSeenEvent;
import com.jme3.ai.agents.events.GameObjectSeenListener;
import java.util.ArrayList;
import java.util.List;
import com.jme3.ai.agents.util.control.Game;
import com.jme3.ai.agents.util.GameObject;

/**
 * Simple look behaviour for NPC. It calls for all behaviour that are added in
 * listeners. That behaviours must implement GameObjectSeenListener.
 *
 * @see GameObjectSeenListener
 *
 * This behaviour can only see GameObject if it is added to game.
 * @see Game#addGameObject(com.jme3.ai.agents.util.GameObject)
 * <br>or for agents
 * @see Game#addAgent(com.jme3.ai.agents.Agent) For seeing things on terrain it
 * uses
 * @see Game#look(com.jme3.ai.agents.Agent, float)
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class SimpleLookBehaviour extends Behaviour {

    /**
     * List of listeners to which behaviours GameObjectSeen should forward to.
     */
    protected List<GameObjectSeenListener> listeners;
    /**
     * Angle in which GameObjects will be seen.
     */
    protected float viewAngle;

    /**
     * @param agent to whom behaviour belongs
     */
    public SimpleLookBehaviour(Agent agent) {
        super(agent);
        listeners = new ArrayList<GameObjectSeenListener>();
        //default value
        viewAngle = FastMath.QUARTER_PI;
    }

    /**
     * @param agent to whom behaviour belongs
     * @param viewAngle angle in which GameObjects will be seen
     */
    public SimpleLookBehaviour(Agent agent, float viewAngle) {
        super(agent);
        listeners = new ArrayList<GameObjectSeenListener>();
        this.viewAngle = viewAngle;
    }

    /**
     * Method for calling all behaviours that are affected by what agent is
     * seeing.
     *
     * @param gameObjectSeen Agent that have been seen
     */
    protected void triggerListeners(GameObject gameObjectSeen) {
        //create GameObjectSeenEvent
        GameObjectSeenEvent event = new GameObjectSeenEvent(agent, gameObjectSeen);
        //forward it to all listeners
        for (GameObjectSeenListener listener : listeners) {
            listener.handleGameObjectSeenEvent(event);
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        List<GameObject> agents = look(agent, viewAngle);
        for (int i = 0; i < agents.size(); i++) {
            triggerListeners(agents.get(i));
        }
    }

    /**
     * Method for determining what agent sees. There is default implementation
     * for agent seeing without obstacles.
     *
     * @see Game#look(com.jme3.ai.agents.Agent, float)
     * @see Game#lookable(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.util.GameObject, float)
     * @param agent - watcher
     * @param viewAngle - viewing angle
     * @return list of all game objects that can be seen by agent
     */
    protected List<GameObject> look(Agent agent, float viewAngle) {
        return Game.getInstance().look(agent, viewAngle);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Adding listener that will trigger when GameObject is seen.
     *
     * @param listener
     */
    public void addListener(GameObjectSeenListener listener) {
        listeners.add(listener);
    }

    /**
     * Removing listener from behaviour.
     *
     * @param listener
     */
    public void removeListener(GameObjectSeenListener listener) {
        listeners.remove(listener);
    }

    /**
     * Removing all listeners from this behaviour.
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * @return angle in which GameObjects will be seen
     */
    public float getViewAngle() {
        return viewAngle;
    }

    /**
     * @param viewAngle angle in which GameObjects will be seen
     */
    public void setViewAngle(float viewAngle) {
        this.viewAngle = viewAngle;
    }
}