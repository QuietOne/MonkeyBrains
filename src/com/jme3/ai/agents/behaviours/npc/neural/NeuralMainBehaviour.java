package behaviours.npc.neural;

import agents.Agent;
import behaviours.npc.Behaviour;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.ArrayList;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * One behaviour to rule them all.<br>
 * Behaviour that use Neural Network to learn proper behaviour activation.
 * Not operational.
 * 
 * @author Tihomir Radosavljević
 */
public class NeuralMainBehaviour extends Behaviour {

    /**
     * List of all behaviours that this behaviour controls.
     */
    private ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();
    /**
     * Neural network for deciding what behaviour should run.
     */
    private MultiLayerPerceptron brain;
    /**
     * Training set for NN.
     */
    private DataSet trainingSet = null;

    public NeuralMainBehaviour(Agent agent) {
        super(agent);
        brain = new MultiLayerPerceptron(new int[]{13, 11, 17, 23});
    }

    public NeuralMainBehaviour(Agent agent, int[] layers) {
        super(agent);
        brain = new MultiLayerPerceptron(layers);
    }

    /**
     * This method is for creating trainingSet, without direct use of Neuroph's
     * methods.
     *
     * @param inputSize number of input neurons
     * @param outputSize number of output neurons
     */
    public void createTrainingSet(int inputSize, int outputSize) {
        trainingSet = new DataSet(inputSize, outputSize);
    }

    /**
     * Expand trainigSet by adding more inputs and outputs.
     *
     * @param input
     * @param output
     */
    public void expandTrainingSet(double[] input, double[] output) {
        if (trainingSet == null) {
            throw new NullPointerException("trainingSet has not been created.");
        } else {
            trainingSet.addRow(input, output);
        }
    }

    /**
     * Generating NN with trainingSet.
     *
     * @see DataSet
     */
    public void learn() {
        if (trainingSet == null) {
            throw new NullPointerException("trainingSet has not been created.");
        } else {
            brain.learn(trainingSet);
        }
    }

    public void addBehaviour(Behaviour behaviour) {
        behaviours.add(behaviour);
    }

    public void removeBehaviour(Behaviour behaviour) {
        behaviours.remove(behaviour);
    }

    @Override
    protected void controlUpdate(float tpf) {
        //calculation
        
        
        brain.setInput(new double[]{1, 2, 3, 4});
        brain.calculate();
        double[] brainOutput = brain.getOutput();
        for (int i = 0; i < brainOutput.length; i++) {
            if (brainOutput[i] > 0.5) {
                //activate i. behaviour
                behaviours.get(i).setEnabled(true);
            } else {
                //deactivate i. behaviour
                behaviours.get(i).setEnabled(false);
            }
        }
        for (Behaviour behaviour : behaviours) {
            behaviour.update(tpf);
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    //don't care about rendering
    }
}
