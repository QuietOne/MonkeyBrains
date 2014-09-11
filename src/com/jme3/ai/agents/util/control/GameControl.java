package com.jme3.ai.agents.util.control;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.input.FlyByCamera;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Base interface for game controls used in game.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public interface GameControl {

    /**
     * Add all inputManagerMapping that will player use.
     */
    public void setInputManagerMapping();
    /**
     * Add all camera settings that will be used in game.
     * @param cam 
     */
    public void setCameraSettings(Camera cam);
    /**
     * Add all fly camera settings that will be used in game.
     * @param flyCam 
     */
    public void setFlyCameraSettings(FlyByCamera flyCam);
    /**
     * Method for marking the end of game. Should also set over to true.
     * @see Game#over
     * @return 
     */
    public boolean finish();
    /**
     * Calculating if the agent won the game.
     * @param agent
     * @return 
     */
    public boolean win(Agent agent);
    /**
     * Restarting all game parameters.
     */
    public void restart();   
    /**
     * Method for creating objects in given area.
     * @param gameObject object that should be created
     * @param area where object will be created
     */
    public void spawn(GameObject gameObject, Vector3f... area);
}
