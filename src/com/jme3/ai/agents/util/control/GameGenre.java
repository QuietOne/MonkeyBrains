package com.jme3.ai.agents.util.control;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import java.util.HashMap;

/**
 * Base interface for game genres.
 *
 * @author Tihomir Radosavljević
 * @version 0.1
 */
public interface GameGenre {

    /**
     * Add all inputManagerMapping that will player use.
     */
    public void loadInputManagerMapping();

    public boolean finish();

    public boolean win(Agent agent);

    public void restart();

    public void spawn(GameObject gameObject, Vector3f... area);

    public void addMoveListener(Agent agent, AnalogListener behaviour);

    public void addMoveListener(Agent agent, ActionListener behaviour);

    public HashMap<String, Behaviour> getPlayerMoveSupportedOperations(Agent agent);
}
