package behaviours.player;

import agents.Agent;
import behaviours.npc.Behaviour;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Game;
import test.LaserBullet;

/**
 * Example of attacking behaviour for player.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class PlayerAttackBehaviour extends Behaviour implements ActionListener {

    /**
     * Operation that should be done.
     * @see util.Game#registerInput() 
     */
    private String operation;
    private Camera cam;

    public PlayerAttackBehaviour(Agent agent, Spatial spatial, Camera cam) {
        super(agent, spatial);
        this.cam = cam;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (operation.equals("Shoot")) {
            //in this demo weapon can only have one bullet (laser) active at the time
            if (agent.getWeapon().getBullet() == null) {
                Vector2f click2d = Game.getInstance().getInputManager().getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                Ray ray = new Ray(click3d, dir);
                Plane ground = new Plane(Vector3f.UNIT_Y, 0);
                Vector3f groundpoint = new Vector3f();
                ray.intersectsWherePlane(ground, groundpoint);
                try {
                    agent.getWeapon().shootAt(groundpoint, tpf);
                } catch (Exception ex) {
                    Logger.getLogger(PlayerAttackBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        operation = name;
        if (isPressed) {
            enabled = true;
        } else {
            enabled = false;
        }
    }
}
