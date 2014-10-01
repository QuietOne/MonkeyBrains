/**
 * Copyright (c) 2014, jMonkeyEngine All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
import com.jme3.math.Vector3f;
import java.util.LinkedList;

/**
 * Simple look behaviour for NPC. It calls for all behaviour that are added in
 * listeners. That behaviours must implement GameEntitySeenListener.
 *
 * @see GameEntitySeenListener
 *
 * This behaviour can only see GameEntity if it is added to game.
 * @see AIAppState#addGameEntity(com.jme3.ai.agents.util.GameEntity)
 * <br>or for agents
 * @see AIAppState#addAgent(com.jme3.ai.agents.Agent)
 * <br><br>
 * It is necessaity to set visibilityRange
 * @see Agent#visibilityRange
 *
 * @author Tihomir Radosavljević
 * @version 1.4.0
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
     * What entities will this behaviour report seeing.
     */
    protected TypeOfWatching typeOfWatching;

    public static enum TypeOfWatching {

        /**
         * Looking only for agents.
         */
        AGENT_WATCHING,
        /**
         * Looking only for game entities.
         */
        GAME_ENTITY_WATCHING,
        /**
         * Looking for agents and game entities.
         */
        WATCH_EVERYTHING;
    }

    /**
     * @param agent to whom behaviour belongs
     */
    public SimpleLookBehaviour(Agent agent) {
        super(agent);
        listeners = new ArrayList<GameEntitySeenListener>();
        //default value
        viewAngle = FastMath.QUARTER_PI;
        typeOfWatching = TypeOfWatching.WATCH_EVERYTHING;
    }

    /**
     * @param agent to whom behaviour belongs
     * @param viewAngle angle in which GameObjects will be seen
     */
    public SimpleLookBehaviour(Agent agent, float viewAngle) {
        super(agent);
        listeners = new ArrayList<GameEntitySeenListener>();
        this.viewAngle = viewAngle;
        typeOfWatching = TypeOfWatching.WATCH_EVERYTHING;
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
    protected void controlUpdate(float tpf) {
        List<GameEntity> gameEntities = look(agent, viewAngle);
        for (int i = 0; i < gameEntities.size(); i++) {
            triggerListeners(gameEntities.get(i));
        }
        //if nothing is seen
        //used for deactivating all behaviours activated with this behaviour
        if (gameEntities.isEmpty()) {
            triggerListeners(null);
        }
    }

    /**
     * Method for determining what agent sees. There is default implementation
     * for agent seeing without obstacles.
     *
     * @param agent - watcher
     * @param viewAngle - viewing angle
     * @return list of all game objects that can be seen by agent
     */
    protected List<GameEntity> look(Agent agent, float viewAngle) {
        List<GameEntity> temp = new LinkedList<GameEntity>();
        //are there seen agents
        if (typeOfWatching == TypeOfWatching.AGENT_WATCHING || typeOfWatching == TypeOfWatching.WATCH_EVERYTHING) {
            List<Agent> agents = AIAppState.getInstance().getAgents();
            for (int i = 0; i < agents.size(); i++) {
                if (agents.get(i).isEnabled()) {
                    if (!agents.get(i).equals(agent) && lookable(agent, agents.get(i))) {
                        temp.add(agents.get(i));
                    }
                }
            }
        }
        if (typeOfWatching == TypeOfWatching.GAME_ENTITY_WATCHING || typeOfWatching == TypeOfWatching.WATCH_EVERYTHING) {
            List<GameEntity> gameEntities = AIAppState.getInstance().getGameEntities();
            for (GameEntity gameEntity : gameEntities) {
                if (gameEntity.isEnabled() && lookable(agent, gameEntity)) {
                    temp.add(gameEntity);
                }
            }
        }
        return temp;
    }

    /**
     * Use with cautious. It works for this example, but it is not general it
     * doesn't include obstacles into calculation.
     *
     * @param observer
     * @param heightAngle
     * @param widthAngle
     * @return
     */
    public boolean lookable(Agent observer, GameEntity gameEntity) {
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

    public TypeOfWatching getTypeOfWatching() {
        return typeOfWatching;
    }

    public void setTypeOfWatching(TypeOfWatching typeOfWatching) {
        this.typeOfWatching = typeOfWatching;
    }
}
