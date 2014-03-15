package test;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.npc.steering.SteeringMainBehaviour;
import com.jme3.ai.agents.behaviours.player.PlayerMainBehaviour;
import com.jme3.ai.agents.util.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;

/**
 *
 * @author Tihomir Radosavljević
 */
public class TestingSteeringBehaviours extends SimpleApplication{

    //defining players
    private Agent<Model> player;
    private Agent<Model> enemySeek;
    //Defining game
    private Game game = Game.getInstance();
    
    private float gameFinishCountDown = 5f;
    //game stats
    private final float attackRange = 40f;
    private final float turnSpeed = 25f;
    private final float laserDamage = 5f;
    private final float terrainSize = 40;

    public static void main(String[] args) {
        TestingSteeringBehaviours app = new TestingSteeringBehaviours();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //defining rootNode for game processing
        game.setRootNode(rootNode);
        //defining input manager
        game.setInputManager(inputManager);

        //DefinedSpatials for graphics for this game
        DefinedSpatials.material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        DefinedSpatials.initializeFloor(terrainSize);
        viewPort.addProcessor(DefinedSpatials.initializeBloom(assetManager));
        flyCam.setMoveSpeed(20);
        //disable the default flyby cam
        flyCam.setEnabled(false);
       
        //initialization of Agents with their names and spatials
        player = new Agent("Player", DefinedSpatials.initializeAgent("Player"));
        enemySeek = new Agent("Enemy", DefinedSpatials.initializeAgent("Enemy"));

        //adding model to player
        player.setModel(new Model(turnSpeed));
        enemySeek.setModel(new Model(turnSpeed));
        
        //adding them to game
        game.addAgent(player);
        game.addAgent(enemySeek, terrainSize * 2- 5, 0, terrainSize * 2 - 5);

        //setting moveSpeed
        player.setMoveSpeed(15);
        enemySeek.setMoveSpeed(40);
        
        //set parameters for steering behaviours
        enemySeek.setMass(80f);
        enemySeek.setMaxForce(3);

        //giving them weapons
        player.setWeapon(new LaserWeapon("laser", player, attackRange, laserDamage));
        enemySeek.setWeapon(new Knife("knife", enemySeek));

        //adding game teams
        player.setTeamName("Player");
        enemySeek.setTeamName("Computers");
        
        //attaching camera to player
        DefinedSpatials.attachCameraTo(player, cam);

        //giving behaviours
        player.setMainBehaviour(new PlayerMainBehaviour(player, cam, terrainSize));
        enemySeek.setMainBehaviour(new SteeringMainBehaviour(enemySeek, player));

        //registering input
        game.registerInput();

        //starting agents
        player.start();
        enemySeek.start();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (game.isOver()) {
            if (gameFinishCountDown <= 0) {
                this.stop();
            } else {
                gameFinishCountDown -= tpf;
                for (Agent agent : game.getAgents()) {
                    if (agent.isAlive()) {
                        BitmapText hudText = new BitmapText(guiFont, false);
                        hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
                        hudText.setColor(ColorRGBA.Red); // font color
                        hudText.setText(agent.getTeamName()+" wins."); // the text
                        hudText.setLocalTranslation(settings.getWidth() / 2 - hudText.getLineWidth() / 2, settings.getHeight() / 2 - hudText.getLineHeight() / 2, 0); // position
                        guiNode.attachChild(hudText);
                        break;
                    }
                }
            }
            
        }
        game.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

}
