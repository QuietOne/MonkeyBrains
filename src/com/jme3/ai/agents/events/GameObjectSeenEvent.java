package com.jme3.ai.agents.events;

import com.jme3.ai.agents.util.GameObject;

/**
 * Event for seen PhysicalObjects.
 *
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class GameObjectSeenEvent extends GameObjectEvent {

    /**
     * GameObject that have been seen.
     */
    private GameObject gameObjectSeen;

    /**
     *
     * @param source object that produce this event (it is usually agent)
     * @param gameObject GameObject that have been seen
     */
    public GameObjectSeenEvent(Object source, GameObject gameObject) {
        super(source);
        this.gameObjectSeen = gameObject;
    }

    /**
     *
     * @return seen GameObject
     */
    public GameObject getGameObjectSeen() {
        return gameObjectSeen;
    }

    /**
     *
     * @param gameObjectSeen seen GameObject
     */
    public void setGameObjectSeen(GameObject gameObjectSeen) {
        this.gameObjectSeen = gameObjectSeen;
    }
}
