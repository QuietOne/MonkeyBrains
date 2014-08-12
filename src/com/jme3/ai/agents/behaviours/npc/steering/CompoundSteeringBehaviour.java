//Copyright (c) 2014, Jesús Martín Berlanga. All rights reserved. Distributed under the BSD licence. Read "com/jme3/ai/license.txt".

package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

/**
 * A steer compound behaviour contains one or more steer behaviours.
 * This class should be used to avoid bugs when adding several
 * steer behaviours to SimpleMainBehaviour. <br> <br>
 * 
 * Fist add several behaviours to make an CompoundBehaviour and then
 * add this compoundBehaviour to an SimpleMainBehaviour. <br><br>
 * 
 * All the steer behaviours added to this class must extend from
 * AbstractSteeringBehaviour. <br><br>
 * 
 * If you have issues related with components cancelling each other out, that 
 * "can be addressed by assigning a priority to components. (For example: first 
 * priority is obstacle avoidance, second is evasion ...) The steering controller 
 * first checks to see if obstacle avoidance returns a non-zero value (indicating 
 * a potential collision), if so it uses that. Otherwise, it moves on to the second 
 * priority behavior, and so on." <br><br>
 * 
 * You can assign the same priority to more than one behaviour, If you do that, the 
 * controller only will move to the next priorities if all the behaviours return
 * zero or a value considered as zero. <br> <br>
 * 
 * @author Jesús Martín Berlanga
 * @version 2.0
 */
public class CompoundSteeringBehaviour extends AbstractStrengthSteeringBehaviour {
        
   /**
    * Ordered list. <br><br>
    * 
    * Nodes are ordered from highest to lowest layer number. Each node has a 
    * behaviour, a integer representing his layer and the min length needed to 
    * consider the behaviour invalid.
    */
    protected class steerBehavioursLayerList
    {   
        protected class steerBehavioursLayerNode
        {   
            private class layerElementData
            {
                private AbstractSteeringBehaviour behaviour;
                private int layer;
                private float minLengthToInvalidSteer;

                public layerElementData( AbstractSteeringBehaviour behaviour, int layer, float minLengthToInvalidSteer )
                {
                    this.behaviour = behaviour;
                    this.layer = layer;
                    this.minLengthToInvalidSteer = minLengthToInvalidSteer;
                }
            }

            private layerElementData data;
            private steerBehavioursLayerNode nextNode;

            private void setData(AbstractSteeringBehaviour behaviour, int layer, float minLengthToInvalidSteer)
            {
                this.data = new layerElementData(behaviour, layer, minLengthToInvalidSteer);
            }
            
            private steerBehavioursLayerNode(  layerElementData data, steerBehavioursLayerNode nextNode)
            {
                this.data = data;
                this.nextNode = nextNode;
            }
            
            private steerBehavioursLayerNode(  AbstractSteeringBehaviour behaviour, int layer, float minLengthToInvalidSteer, steerBehavioursLayerNode nextNode)
            {
                this.data = new layerElementData(behaviour, layer, minLengthToInvalidSteer);
                this.nextNode = nextNode;
            }
        }
        
        private steerBehavioursLayerNode head = null;
        private steerBehavioursLayerNode pointer = null;
               
        /** To optimize the process speed You need to add the behaviours from lowest to highest layer number. */
        public void add(AbstractSteeringBehaviour behaviour, int layer, float minLengthToInvalidSteer)
        {
            if(head == null)
            {
                head = new steerBehavioursLayerNode(behaviour, layer, minLengthToInvalidSteer, null);
                return;
            }
                
            steerBehavioursLayerNode aux = head;
            
            while( aux.data.layer > layer && aux.nextNode != null)
                aux = aux.nextNode;
                
            if(aux.data.layer > layer)
            {
                aux.nextNode = new steerBehavioursLayerNode(behaviour, layer, minLengthToInvalidSteer, null);
            }
            else
            {
                steerBehavioursLayerNode.layerElementData nextData = aux.data;
                steerBehavioursLayerNode nextNode = aux.nextNode;

                aux.setData(behaviour, layer, minLengthToInvalidSteer);
                aux.nextNode = new steerBehavioursLayerNode(nextData, nextNode);
            }
        }
        
        public void moveAtBeginning()
        {
           this.pointer = this.head;
        }
        
        public void moveNext()
        {
           steerBehavioursLayerNode current =  this.pointer;
           if(current != null) this.pointer = this.pointer.nextNode;    
        }
        
        public boolean nullPointer() { return this.pointer == null; }
        
        public steerBehavioursLayerNode getPointer() { return this.pointer; }
        public void setPointer(steerBehavioursLayerNode pointer) { this.pointer = (steerBehavioursLayerNode) pointer; }
        
        public AbstractSteeringBehaviour getBehaviour()
        {   
            return this.pointer.data.behaviour;
        }
        
        public int getLayer()
        {          
            return this.pointer.data.layer;
        }
        
        public float getMinLengthToInvalidSteer()
        {
            return this.pointer.data.minLengthToInvalidSteer;
        }
    }
    
    /** Partial behaviours */
    protected steerBehavioursLayerList behaviours;
    
    /** @see  AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent)  */
    public CompoundSteeringBehaviour(Agent agent) 
    { 
        super(agent);
        this.behaviours = new steerBehavioursLayerList();
    }
    
    /** @see AbstractSteeringBehaviour#AbstractSteeringBehaviour(com.jme3.ai.agents.Agent, com.jme3.scene.Spatial)  */
    public CompoundSteeringBehaviour(Agent agent, Spatial spatial) 
    { 
        super(agent, spatial);
        this.behaviours = new steerBehavioursLayerList();
    }
    
    /**
     * Adds a behaviour to the compound behaviour.  <br><br>
     * 
     * Layer and the min length to consider the behaviour invalid are 0 by default.
     * 
     * @param behaviour Behaviour that you want to add
     */
    public void addSteerBehaviour (AbstractSteeringBehaviour behaviour) {
        this.behaviours.add(behaviour, 0, 0);
        behaviour.setIsAPartialSteer(true);
        behaviour.setContainer(this);
    }  
    
    /**
     *  To optimize the process speed add the behaviours with the lowest priority first. 
     * 
     * @see CompoundSteeringBehaviour#addSteerBehaviour(com.jme3.ai.agents.behaviours.npc.steering.AbstractSteeringBehaviour)
     * 
     * @param priority This behaviour will be processed If all higher priority behaviours can be considered inactives
     * @param minLengthToInvalidSteer If the behaviour steer force length is less than this value It will be considered inactive
     */
    public void addSteerBehaviour (AbstractSteeringBehaviour behaviour, int priority, float minLengthToInvalidSteer) {
        this.behaviours.add(behaviour, priority, minLengthToInvalidSteer);
        behaviour.setIsAPartialSteer(true);
        behaviour.setContainer(this);
    }
   
    /**
     * Calculates the composed steering force. The composed force is the sumatory
     * of the behaviours steering forces.
     * 
     * @return The composed steering force.
     */
    @Override
    protected Vector3f calculateFullSteering() {
        
        Vector3f totalForce = new Vector3f();
        
        this.behaviours.moveAtBeginning();
        //steerBehavioursLayerList.steerBehavioursLayerNode savedNod = null; //DEBUG
        
        if(!this.behaviours.nullPointer())
        {
            int currentLayer = this.behaviours.getLayer();
            int inLayerCounter = 0;
            int validCounter = 0;

            while(!this.behaviours.nullPointer())
            {
                if(this.behaviours.getLayer() != currentLayer) //We have finished the last layer, check If it was a valid layer
                {
                   if(inLayerCounter == validCounter) break; //If we have a valid layer, return the force
                   else totalForce = new Vector3f(); //If not, reset the total force
                          
                   currentLayer = this.behaviours.getLayer();
                   inLayerCounter = 0;
                   validCounter = 0;
                }
                
                Vector3f force = this.calculatePartialForce(this.behaviours.getBehaviour());             
                if(force.length() > this.behaviours.getMinLengthToInvalidSteer()) validCounter++; 
                totalForce = totalForce.add(force);

                inLayerCounter++;
                //savedNod = behaviours.getPointer();//DEBUG
                this.behaviours.moveNext();
            }
        }
        
        /* 
        //DEBUG
        if(savedNod != null) 
        {
            behaviours.setPointer(savedNod);
            System.out.println("Active Layer :" + behaviours.getLayer());
        } 
        */
        return totalForce;
    }
    
    /**
     * Calculates the steering force of a single behaviour
     * @param behaviour The behaviour.
     * @return The steering force of that behaviour
     */
    protected Vector3f calculatePartialForce(AbstractSteeringBehaviour behaviour) {
        return behaviour.calculateSteering();
    }


    protected void controlRender(RenderManager rm, ViewPort vp) {  }

    /**
     * Usual update pattern for steering behaviours.
     *
     * @param tpf
     */
    @Override
    protected void controlUpdate(float tpf) 
    {    
        this.behaviours.moveAtBeginning();

        while(!this.behaviours.nullPointer())
        {
            this.behaviours.getBehaviour().setTPF(tpf);
            this.behaviours.moveNext();
        }

        super.controlUpdate(tpf);
    }
    
}