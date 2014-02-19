package test;

import util.Game;
import agents.Agent;
import behaviours.npc.SeekNDestroyBehaviour;
import behaviours.player.PlayerMainBehaviour;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;

/**
 * Testing demo game for this agent framework.
 */
public class Main extends SimpleApplication {

    //defining players
    private Agent<Model> player;
    private Agent<Model> enemy;
    private Agent<Model> enemyNeural;
    //Defining game
    private Game game = Game.getInstance();
    
    private float gameFinishCountDown = 5f;
    //game stats
    private final float attackRange = 40f;
    private final float turnSpeed = 25f;
    private final float laserDamage = 5f;
    private final float terrainSize = 40;

    public static void main(String[] args) {
        Main app = new Main();
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
        enemy = new Agent("Enemy", DefinedSpatials.initializeAgent("Enemy"));
        enemyNeural = new Agent("Mental", DefinedSpatials.initializeAgent("Mental"));

        //adding model to player
        player.setModel(new Model(turnSpeed));
        enemy.setModel(new Model(turnSpeed));
        enemyNeural.setModel(new Model(turnSpeed));
        
        //adding them to game
        game.addAgent(player);
        game.addAgent(enemy, terrainSize * 2- 5, 0, terrainSize * 2 - 5);
        game.addAgent(enemyNeural, -terrainSize * 2 + 5, 0, -terrainSize * 2 + 5);

        //setting moveSpeed
        player.setMoveSpeed(15);
        enemy.setMoveSpeed(8);
        enemyNeural.setMoveSpeed(8);

        //giving them weapons
        //player.setWeapon(new Cannon("cannon", player, 70f, 10f));
        player.setWeapon(new LaserWeapon("laser", player, attackRange, laserDamage));
        enemy.setWeapon(new LaserWeapon("laser", enemy, attackRange, laserDamage));
        enemyNeural.setWeapon(new LaserWeapon("laser", enemyNeural, attackRange, laserDamage));

        enemy.setVisibilityRange(150f);
        enemyNeural.setVisibilityRange(150f);
        //attaching camera to player
        DefinedSpatials.attachCameraTo(player, cam);

        //giving behaviours
        player.setMainBehaviour(new PlayerMainBehaviour(player, cam, terrainSize));
        enemy.setMainBehaviour(new SeekNDestroyBehaviour(enemy, terrainSize));
        enemyNeural.setMainBehaviour(new SeekNDestroyBehaviour(enemyNeural, terrainSize));
        
        //Neural not yet finished
        //enemyNeural.setMainBehaviour(new NeuralMainBehaviour(enemyNeural));

        //registering input
        game.registerInput();

        //starting agents
        player.start();
        enemy.start();
        enemyNeural.start();
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
                        hudText.setText(agent.getName()+" wins."); // the text
                        hudText.setLocalTranslation(settings.getWidth() / 2 - hudText.getLineWidth() / 2, settings.getHeight() / 2 - hudText.getLineHeight() / 2, 0); // position
                        guiNode.attachChild(hudText);
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
