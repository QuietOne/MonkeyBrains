package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.FastMath;
import com.jme3.ai.agents.events.GameEntitySeenEvent;
import com.jme3.ai.agents.events.GameEntitySeenListener;
import java.util.ArrayList;
import java.util.List;
import com.jme3.ai.agents.util.control.AIAppState;
import com.jme3.ai.agents.util.GameEntity;

/**
 * Simple look behaviour for NPC. It calls for all behaviour that are added in
 * listeners. That behaviours must implement GameEntitySeenListener.
 *
 * @see GameEntitySeenListener
 *
 * This behaviour can only see GameEntity if it is added to game.
 * @see AIAppState#addGameEntity(com.jme3.ai.agents.util.GameEntity)
 * <br>or for agents
 * @see AIAppState#addAgent(com.jme3.ai.agents.Agent) For seeing things on
 * terrain it uses
 * @see AIAppState#look(com.jme3.ai.agents.Agent, float)
 *
 * @author Tihomir Radosavljević
 * @version 1.0.1
 */
public class SimpleLookBehaviour extends Behaviour {

    /**
     * List of listeners to which behaviours GameObjectSeen should forward to.
     */
    protected List<GameEntitySeenListener> listeners;
    /**
     * Angle in which GameObjects will be seen.
     */
    protected float viewAngle;

    /**
     * @param agent to whom behaviour belongs
     */
    public SimpleLookBehaviour(Agent agent) {
        super(agent);
        listeners = new ArrayList<GameEntitySeenListener>();
        //default value
        viewAngle = FastMath.QUARTER_PI;
    }

    /**
     * @param agent to whom behaviour belongs
     * @param viewAngle angle in which GameObjects will be seen
     */
    public SimpleLookBehaviour(Agent agent, float viewAngle) {
        super(agent);
        listeners = new ArrayList<GameEntitySeenListener>();
        this.viewAngle = viewAngle;
    }

    /**
     * Method for calling all behaviours that are affected by what agent is
     * seeing.
     *
     * @param gameEntitySeen Agent that have been seen
     */
    protected void triggerListeners(GameEntity gameEntitySeen) {
        //create GameEntitySeenEvent
        GameEntitySeenEvent event = new GameEntitySeenEvent(agent, gameEntitySeen);
        //forward it to all listeners
        for (GameEntitySeenListener listener : listeners) {
            listener.handleGameEntitySeenEvent(event);
        }
    }

    @Override
    protected final void controlUpdate(float tpf) {
        List<GameEntity> agents = look(agent, viewAngle);
        for (int i = 0; i < agents.size(); i++) {
            triggerListeners(agents.get(i));
        }
    }

    /**
     * Method for determining what agent sees. There is default implementation
     * for agent seeing without obstacles.
     *
     * @see AIAppState#look(com.jme3.ai.agents.Agent, float)
     * @see AIAppState#lookable(com.jme3.ai.agents.Agent,
     * com.jme3.ai.agents.util.GameEntity, float)
     * @param agent - watcher
     * @param viewAngle - viewing angle
     * @return list of all game objects that can be seen by agent
     */
    protected List<GameEntity> look(Agent agent, float viewAngle) {
        return AIAppState.getInstance().look(agent, viewAngle);
    }

    /**
     * Adding listener that will trigger when GameEntity is seen.
     *
     * @param listener
     */
    public void addListener(GameEntitySeenListener listener) {
        listeners.add(listener);
    }

    /**
     * Removing listener from behaviour.
     *
     * @param listener
     */
    public void removeListener(GameEntitySeenListener listener) {
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
