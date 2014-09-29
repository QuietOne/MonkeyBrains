package com.jme3.ai.agents.events;

import com.jme3.ai.agents.util.GameEntity;

/**
 * Event for seen GameEntities.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public class GameEntitySeenEvent extends GameEntityEvent {

    /**
     * GameEntity that have been seen.
     */
    private GameEntity gameEntitySeen;

    /**
     *
     * @param source object that produce this event (it is usually agent)
     * @param gameEntity GameEntity that have been seen
     */
    public GameEntitySeenEvent(Object source, GameEntity gameEntity) {
        super(source);
        this.gameEntitySeen = gameEntity;
    }

    /**
     *
     * @return seen GameEntity
     */
    public GameEntity getGameEntitySeen() {
        return gameEntitySeen;
    }

    /**
     *
     * @param gameEntitySeen seen GameEntity
     */
    public void setGameEntitySeen(GameEntity gameEntitySeen) {
        this.gameEntitySeen = gameEntitySeen;
    }
}
