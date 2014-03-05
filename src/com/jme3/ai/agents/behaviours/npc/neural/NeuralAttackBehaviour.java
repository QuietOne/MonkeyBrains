package com.jme3.ai.agents.behaviours.npc.neural;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.net.URL;
import java.util.ArrayList;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 * Example of neural attack behaviour fon NPC.
 * @author Tihomir Radosavljević
 * @version 1.0
 */
public class NeuralAttackBehaviour extends Behaviour{

    /**
     * Targeted agent.
     */
    private Agent target;
    /**
     * Neural network that calculate future position of enemy agents. 
     */
    private MultiLayerPerceptron brain;
    /**
     * Data set needed for training network and for testing it.
     */
    private DataSet dataSet;
    /**
     * List of enemy positions. It is used for predicting next one.
     */
    private ArrayList<Vector3f> enemyPositions;

    public NeuralAttackBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        //defining type of NN (Neural network)
        brain = new MultiLayerPerceptron(TransferFunctionType.TANH, 20, 10, 2);
        //creating dataSet from url
        URL url = getClass().getResource("TrainingSet.csv");
        dataSet = DataSet.createFromFile(url.getPath(), 20, 2, " ");
        //training NN
        brain.learn(dataSet);
        enemyPositions = new ArrayList<Vector3f>();
    }

    public void setTarget(Agent target) {
        this.target = target;
    }
        
    // the main logic of behaviour goes here 
    @Override
    protected void controlUpdate(float tpf) {
        if (agent.getWeapon().getBullet() == null) {
            try {
                if (target != null) {
                    if (!target.isAlive()) {
                        target = null;
                        return;
                    }
                }
                //adding position of enemy
                enemyPositions.add(new Vector3f(target.getLocalTranslation()));
                //if there have been more moves by enemy, learn his moves
                if (enemyPositions.size() > 11) {
                    learnBySeeing();
                    //remove last added position
                    enemyPositions.remove(0);
                }
                //set agents movements
                brain.setInput(enemyPositions.get(0).getX()/1000,
                        enemyPositions.get(1).getX()/1000,
                        enemyPositions.get(2).getX()/1000,
                        enemyPositions.get(3).getX()/1000,
                        enemyPositions.get(4).getX()/1000,
                        enemyPositions.get(5).getX()/1000,
                        enemyPositions.get(6).getX()/1000,
                        enemyPositions.get(7).getX()/1000,
                        enemyPositions.get(8).getX()/1000,
                        enemyPositions.get(9).getX()/1000,
                        enemyPositions.get(0).getZ()/1000,
                        enemyPositions.get(1).getZ()/1000,
                        enemyPositions.get(2).getZ()/1000,
                        enemyPositions.get(3).getZ()/1000,
                        enemyPositions.get(4).getZ()/1000,
                        enemyPositions.get(5).getZ()/1000,
                        enemyPositions.get(6).getZ()/1000,
                        enemyPositions.get(7).getZ()/1000,
                        enemyPositions.get(8).getZ()/1000,
                        enemyPositions.get(9).getZ()/1000);
                //calculate his next move
                double[] vector = brain.getOutput();
                //set vector for his movement
                Vector3f futurePosition = new Vector3f((float) vector[0] * 1000, 3, (float) vector[1] * 1000);
                //shoot at where it thinks enemy will be
                agent.getWeapon().shootAt(futurePosition, tpf);
            } catch (Exception ex) {
                //Logger.getLogger(AttackBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }
    
    /**
     * Method for training NN, during the game.
     */
    private void learnBySeeing() {
        double[] inputs = new double[20];
        double[] outputs = new double[2];
        for (int i = 0; i < 10; i++) {
            inputs[i] = enemyPositions.get(i).getX()/1000;
            inputs[i + 10] = enemyPositions.get(i).getZ()/1000;
        }
        outputs[0] = enemyPositions.get(10).getX()/1000;
        outputs[1] = enemyPositions.get(10).getZ()/1000;
        dataSet.addRow(inputs, outputs);
        brain.learn(dataSet);
        //removing dataSet if it is too large, because it can take time training
        //it if set is too large
        while (dataSet.size() > 1000) {
            dataSet.removeRowAt(0);
        }
    }

}
