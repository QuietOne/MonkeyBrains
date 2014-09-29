package com.jme3.ai.agents.util.control;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.util.GameEntity;
import com.jme3.input.FlyByCamera;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Base interface for game controls used in game.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.1
 */
public interface AIControl {

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
     * Method for marking the end of game. Should also stop the game.
     * @see Game#stop() 
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
     * @param gameEntity object that should be created
     * @param area where object will be created
     */
    public void spawn(GameEntity gameEntity, Vector3f... area);
}
