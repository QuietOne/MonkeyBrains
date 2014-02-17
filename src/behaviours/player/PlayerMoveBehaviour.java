package behaviours.player;

import agents.Agent;
import behaviours.npc.Behaviour;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import util.Game;
import test.Model;

/**
 * Example of moving behaviour for player.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class PlayerMoveBehaviour extends Behaviour implements AnalogListener{

    /**
     * Name of operation that should be done.
     * @see util.Game#registerInput()
     */
    private String operation;
    /**
     * Size of terrain on which agents move.
     */
    private float terrainSize;
    
    /**
     * @param agent Agent to whom is added this behaviour
     * @param spatial active spatial durring moving
     */
    public PlayerMoveBehaviour(Agent agent, Spatial spatial, float terrainSize) {
        super(agent, spatial);
        this.terrainSize = terrainSize;
    }

    @Override
    protected void controlUpdate(float tpf) {
        float turnSpeed = ((Model) agent.getModel()).getTurnSpeed();
        // you can't move if game is over
        if (Game.getInstance().isOver()) {
            enabled = false;
        }
        Vector3f oldPos = agent.getLocalTranslation().clone();
        if (operation.equals("moveForward")) {
            agent.getSpatial().move(agent.getLocalRotation().mult(new Vector3f(0, 0, agent.getMoveSpeed() * tpf)));
            if (agent.getLocalTranslation().x > terrainSize * 2 || agent.getLocalTranslation().z > terrainSize * 2
                    || agent.getLocalTranslation().x < -terrainSize * 2 || agent.getLocalTranslation().z < -terrainSize * 2) {
                agent.setLocalTranslation(oldPos);
            }
        }
        if (operation.equals("moveBackward")) {
            agent.getSpatial().move(agent.getLocalRotation().mult(new Vector3f(0, 0, -agent.getMoveSpeed() * tpf)));
            if (agent.getLocalTranslation().x > terrainSize * 2 || agent.getLocalTranslation().z > terrainSize * 2
                    || agent.getLocalTranslation().x < -terrainSize * 2 || agent.getLocalTranslation().z < -terrainSize * 2) {
                agent.setLocalTranslation(oldPos);
            }
        }
        if (operation.equals("moveRight")) {
            agent.getSpatial().rotate(0, -(FastMath.DEG_TO_RAD * tpf) * turnSpeed, 0);
        }
        if (operation.equals("moveLeft")) {
            agent.getSpatial().rotate(0, (FastMath.DEG_TO_RAD * tpf) * turnSpeed, 0);
        }
        enabled = false;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

    public void onAnalog(String name, float value, float tpf) {
        operation = name;
        if (value!=0) {
            enabled = true;
        }
    }

}
