package test;

import util.Game;
import agents.Agent;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;

/**
 * This class is not used for use of framework, but is only used to store
 * methods for geometries and for input methods, that are used in this game. The
 * main reason for this is to show usage of this framework for this simple demo
 * game without redundant details.
 *
 * @author Tihomir Radosavljević
 */
public class DefinedSpatials {

    public static Material material;
    private static float maxBlur = 2.7f;
    private static BloomFilter bf;

    /**
     * Creating spatial for agent.
     * @return 
     */
    public static Node initializeAgent(String name) {
        // create a robot for the player
        Node player = createRobot(name);
        player.setUserData("health", 100f);

        // add simple healthbar
        BillboardControl billboard = new BillboardControl();
        Geometry healthbar = new Geometry("healthbar", new Quad(4f, 0.2f));
        Material mathb = material.clone();
        mathb.setColor("Color", ColorRGBA.Red);
        healthbar.setMaterial(mathb);
        player.attachChild(healthbar);
        healthbar.center();
        healthbar.move(4, 1, -4);
        healthbar.addControl(billboard);

        // put player in center
        player.move(-2, 0, 0);
        player.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        return player;
    }

    /**
     * Creating step by step robot spatial.
     * @param name
     * @return 
     */
    private static Node createRobot(String name) {
        Node res = new Node(name);

        // create the feet
        Box leftFoot = new Box(0.5f, 0.2f, 1.5f);
        Box rightFoot = new Box(0.5f, 0.2f, 1.5f);
        Geometry geomlf = new Geometry("leftFoot", leftFoot);
        Geometry geomrf = new Geometry("rightFoot", rightFoot);
        Material matf = material.clone();
        matf.setColor("Color", ColorRGBA.DarkGray);
        geomlf.setMaterial(matf);
        geomrf.setMaterial(matf);
        geomlf.center().move(new Vector3f(1, 0.21f, 0));
        geomrf.center().move(new Vector3f(-1, 0.21f, 0));

        res.attachChild(geomlf);
        res.attachChild(geomrf);

        // create the body
        Cylinder body = new Cylinder(8, 16, 1, 3);
        Geometry bodygeom = new Geometry("body", body);
        Material matb = material.clone();
        matb.setColor("Color", ColorRGBA.Gray);
        bodygeom.setMaterial(matb);
        bodygeom.center().move(new Vector3f(0, 1.92f, 0));
        bodygeom.rotate(FastMath.DEG_TO_RAD * 90, 0, 0);
        res.attachChild(bodygeom);

        // create the head
        Sphere head = new Sphere(16, 32, 1.5f);
        Geometry headgeom = new Geometry("head", head);
        Material math = material.clone();
        math.setColor("Color", ColorRGBA.Gray);
        headgeom.setMaterial(math);
        headgeom.center().move(new Vector3f(0, 4.42f, 0));
        res.attachChild(headgeom);

        // create a "nose" to see where robot is heading
        Box nose = new Box(0.2f, 0.2f, 0.2f);
        Geometry nosegeom = new Geometry("head", nose);
        Material matn = material.clone();
        matn.setColor("Color", ColorRGBA.Orange);
        matn.setColor("GlowColor", ColorRGBA.Orange);
        nosegeom.setMaterial(matn);
        nosegeom.center().move(new Vector3f(0, 4.42f, 1.5f));
        res.attachChild(nosegeom);

        return res;
    }

    /**
     * Initializing bloom.
     * @param assetManager
     * @return 
     */
    public static FilterPostProcessor initializeBloom(AssetManager assetManager) {
        // add a glow effect
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        bf = new BloomFilter(BloomFilter.GlowMode.Objects);
        bf.setExposurePower(18f);
        bf.setBloomIntensity(1.0f);
        bf.setBlurScale(maxBlur);
        //bf.setExposureCutOff(1f);
        bf.setDownSamplingFactor(2.2f);
        fpp.addFilter(bf);
        return fpp;
    }

    /**
     * Initializing floor.
     */
    public static void initializeFloor(float terrainSize) {
        Game game = Game.getInstance();
        // create a floor
        float gridsize = terrainSize / 10;
        Box bx = new Box(gridsize / 2, 0.02f, 0.02f);
        Material matx = material.clone();
        matx.setColor("Color", ColorRGBA.Cyan);
        matx.setColor("GlowColor", ColorRGBA.Blue);
        Box by = new Box(0.02f, 0.02f, gridsize / 2);
        Material maty = material.clone();
        maty.setColor("Color", ColorRGBA.Cyan);
        maty.setColor("GlowColor", ColorRGBA.Blue);

        Box floor = new Box(gridsize / 2 - 0.01f, 0.01f, gridsize / 2 - 0.01f);
        Material matfloor = material.clone();
        matfloor.setColor("Color", ColorRGBA.LightGray);

        for (int x = (int) -gridsize * 5; x <= gridsize * 5; x++) {
            for (int y = (int) -gridsize * 5; y <= gridsize * 5; y++) {
                Geometry geomx = new Geometry("Grid", bx);
                geomx.setMaterial(matx);
                geomx.center().move(new Vector3f(x * gridsize, 0, y * gridsize + gridsize / 2));

                game.getRootNode().attachChild(geomx);

                if (y == (int) -gridsize * 5) {
                    Geometry geomxend = geomx.clone();
                    geomxend.center().move(new Vector3f(x * gridsize, 0, y * gridsize - gridsize / 2));
                    game.getRootNode().attachChild(geomxend);
                }

                Geometry geomy = new Geometry("Grid", by);
                geomy.setMaterial(maty);
                geomy.center().move(new Vector3f(x * gridsize + gridsize / 2, 0, y * gridsize));

                game.getRootNode().attachChild(geomy);

                if (x == (int) -gridsize * 5) {
                    Geometry geomyend = geomy.clone();
                    geomyend.center().move(new Vector3f(x * gridsize - gridsize / 2, 0, y * gridsize));
                    game.getRootNode().attachChild(geomyend);
                }

                Geometry geomfloor = new Geometry("Floor", floor);
                geomfloor.setMaterial(matfloor);
                geomfloor.center().move(new Vector3f(x * gridsize, 0, y * gridsize));

                game.getRootNode().attachChild(geomfloor);
            }
        }
    }

    /**
     * Attaching camera to agent.
     * @param agent
     * @param cam 
     */
    public static void attachCameraTo(Agent agent, Camera cam) {
        //create the camera Node
        CameraNode camNode = new CameraNode("Camera Node", cam);
        //this mode means that camera copies the movements of the target
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        //attach the camNode to the target
        ((Node) agent.getSpatial()).attachChild(camNode);
        //move camNode, e.g. behind and above the target
        camNode.setLocalTranslation(new Vector3f(0, 6, -18));
        //rotate the camNode to look at the target
        camNode.lookAt(agent.getSpatial().getLocalTranslation(), Vector3f.UNIT_Y);
        camNode.setLocalTranslation(new Vector3f(0, 12, -22));
    }


    /**
     * Creating bullet and attaching it to shooter.
     * @param shooter to whom is attached
     * @param direction direction of bullet
     * @param length length of bullet
     * @return spatilal of bullet
     */
    public static Geometry initializeLaserBullet(Agent shooter, Vector3f direction, float length) {
        float laserLength = length;

        //this is part where geometry begins
        Cylinder laser = new Cylinder(4, 8, 0.02f, laserLength);
        Geometry laserBeam = new Geometry("laserbeam", laser);
        Material matlaser = DefinedSpatials.material.clone();
        matlaser.setColor("Color", ColorRGBA.Red);
        matlaser.setColor("GlowColor", ColorRGBA.Orange);
        laserBeam.setMaterial(matlaser);
        // attach laserbeam to player so it moves with player
        ((Node) shooter.getSpatial()).attachChild(laserBeam);
        // center laserbeam on players origin
        laserBeam.center();
        // make the laserbeam point towards clicked spot
        laserBeam.lookAt(direction, Vector3f.UNIT_Z);
        // move laserbeam up so it does not shoot on ground level, but from player model
        laserBeam.move(new Vector3f(0, 3, 0));
        // move laserbeam forward because cylinder is created with center at player origin
        laserBeam.move(laserBeam.getLocalRotation().mult(new Vector3f(0, 0, laserLength / 2)));
        return laserBeam;
    }
}
