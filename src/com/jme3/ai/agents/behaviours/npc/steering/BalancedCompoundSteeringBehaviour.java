//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 * Each force added to this container is reduced in relation with a
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
 * @version 2.0
 */
public class BalancedCompoundSteeringBehaviour extends CompoundSteeringBehaviour {
    
    private boolean strengthIsBalanced;
    
    private Vector3f totalForce;
    private List<Vector3f> partialForces = new ArrayList<Vector3f>();
    private int numberOfPartialForcesAlreadyCalculated = 0;
    private int numberOfBehaviours;
    
    /**
     * Turn on or off the balance. The balance is activated by default.
     */
    public void  setStrengthIsBalanced(boolean strengthIsBalanced) { this.strengthIsBalanced = strengthIsBalanced; }
    
    /** @see  AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)  */
    public BalancedCompoundSteeringBehaviour(Agent agent) {
        super(agent);
        this.strengthIsBalanced = true;
    }
    
    /** @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)  */
    public BalancedCompoundSteeringBehaviour(Agent agent, Spatial spatial) {
        super(agent, spatial);
        this.strengthIsBalanced = true;
    }
       
    /** @see BalancedCompoundSteeringBehaviour#addSteerBehaviour(com.jme3.ai.agents.behaviours.npc.steering.AbstractSteeringBehaviour)   */
    @Override
    public void addSteerBehaviour (AbstractSteeringBehaviour behaviour) {
        super.addSteerBehaviour(behaviour);
        this.numberOfBehaviours++;
    }
    
    
    /** @see CompoundSteeringBehaviour#calculatePartialForce(com.jme3.ai.agents.behaviours.npc.steering.AbstractSteeringBehaviour)  */
    @Override
    protected Vector3f calculatePartialForce(AbstractSteeringBehaviour behaviour) 
    {
        this.calculateTotalForce();
        Vector3f partialForce = this.partialForces.get(this.numberOfPartialForcesAlreadyCalculated);
        
        if(this.strengthIsBalanced && this.totalForce.length() > 0)
        {
            partialForce.mult(partialForce.length()
                    / this.totalForce.length());
        }
        
        
        this.partialForceCalculated();
             
        return partialForce;
    }
    
    //Calculates the total force if it is not calculated
    private void calculateTotalForce() 
    {    
        if(this.numberOfPartialForcesAlreadyCalculated == 0)
        {
            Vector3f totalForceAux = new Vector3f();
            
            steerBehavioursLayerList.steerBehavioursLayerNode currentPointer = this.behaviours.getPointer(); //We do not want to modify the current pointer
            
            this.behaviours.moveAtBeginning();

            while(!this.behaviours.nullPointer())
            {
                Vector3f partial = this.behaviours.getBehaviour().calculateSteering();
                this.partialForces.add(partial);
                totalForceAux = totalForceAux.add(partial);
                
                this.behaviours.moveNext();
            }
            
            this.behaviours.setPointer(currentPointer); //Restore the previous pointer
               
            this.totalForce = totalForceAux;
        }
        
    }
    
    //Reset the forces if we have finished with all the forces
    private void partialForceCalculated() {
        
        this.numberOfPartialForcesAlreadyCalculated++;
        
        if(this.numberOfPartialForcesAlreadyCalculated >= this.numberOfBehaviours)
        {
            this.numberOfPartialForcesAlreadyCalculated = 0;
            this.partialForces.clear();
        }
        
    }
    
}
