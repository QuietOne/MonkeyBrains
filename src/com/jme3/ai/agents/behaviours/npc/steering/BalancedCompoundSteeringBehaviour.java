//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved.
//Distributed under the BSD licence. Read "com/jme3/ai/license.txt".
package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

/**
 * Each force generated inside this container is reduced in relation with a
 * proportion factor. <br> <br>
 *
 * "Proportion factor" = "Partial Force" / "Total container force" <br><br>
 *
 * The balace is activated by default. This balance can be "desactivated" with
 * setStrengthIsBalanced(false).
 *
 * @see CompoundSteeringBehaviour
 *
 * @author Jesús Martín Berlanga
 * @version 2.0.1
 */
public class BalancedCompoundSteeringBehaviour extends CompoundSteeringBehaviour {

    private boolean strengthIsBalanced;
    private Vector3f totalForce;
    private List<Vector3f> partialForces;
    private int numberOfPartialForcesAlreadyCalculated = 0;
    private int numberOfBehaviours;

    /**
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)
     */
    public BalancedCompoundSteeringBehaviour(Agent agent) {
        super(agent);
        this.strengthIsBalanced = true;
        partialForces = new ArrayList<Vector3f>();
    }

    /**
     * @see
     * AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent,
     * com.jme3.scene.Spatial)
     */
    public BalancedCompoundSteeringBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        this.strengthIsBalanced = true;
        partialForces = new ArrayList<Vector3f>();
    }

    /**
     * @see
     * BalancedCompoundSteeringBehaviour#addSteerBehaviour(com.jme3.ai.agents.behaviours.npc.steering.AbstractSteeringBehaviour)
     */
    @Override
    public void addSteerBehaviour(AbstractSteeringBehaviour behaviour) {
        super.addSteerBehaviour(behaviour);
        this.numberOfBehaviours++;
    }

    /**
     * Turn on or off the balance. The balance is activated by default.
     */
    public void setStrengthIsBalanced(boolean strengthIsBalanced) {
        this.strengthIsBalanced = strengthIsBalanced;
    }

    /**
     * @see
     * CompoundSteeringBehaviour#calculatePartialForce(com.jme3.ai.agents.behaviours.npc.steering.AbstractSteeringBehaviour)
     */
    @Override
    protected Vector3f calculatePartialForce(AbstractSteeringBehaviour behaviour) {
        this.calculateTotalForce();
        Vector3f partialForce = this.partialForces.get(this.numberOfPartialForcesAlreadyCalculated);

        if (this.strengthIsBalanced && this.totalForce.length() > 0) {
            partialForce.mult(partialForce.length()
                    / this.totalForce.length());
        }

        this.partialForceCalculated();
        return partialForce;
    }

    /**
     * Calculates the total force if it is not calculated
     */
    protected void calculateTotalForce() {
        if (numberOfPartialForcesAlreadyCalculated == 0) {
            Vector3f totalForceAux = new Vector3f();

            //We do not want to modify the current pointer
            steerBehavioursLayerList.steerBehavioursLayerNode currentPointer = this.behaviours.getPointer();

            behaviours.moveAtBeginning();

            while (!behaviours.nullPointer()) {
                Vector3f partial = behaviours.getBehaviour().calculateSteering();
                partialForces.add(partial);
                totalForceAux = totalForceAux.add(partial);
                behaviours.moveNext();
            }

            //Restore the previous pointer
            this.behaviours.setPointer(currentPointer);
            this.totalForce = totalForceAux;
        }

    }

    /**
     * Reset the forces if we have finished with all the forces
     */
    protected void partialForceCalculated() {
        numberOfPartialForcesAlreadyCalculated++;

        if (numberOfPartialForcesAlreadyCalculated >= this.numberOfBehaviours) {
            numberOfPartialForcesAlreadyCalculated = 0;
            partialForces.clear();
        }
    }
}
