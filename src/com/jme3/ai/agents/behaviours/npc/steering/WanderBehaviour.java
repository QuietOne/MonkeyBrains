package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.Random;

/**
 *
 * @author Tihomir Radosavljević
 */
public class WanderBehaviour extends Behaviour{

    private Vector3f targetPosition;
    private Vector3f velocity;
    private float time;
    
    public WanderBehaviour(Agent agent) {
        super(agent);
        targetPosition = new Vector3f();
        velocity = new Vector3f();
        time = 2f;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        float terrainSize = 40f;
        changeTargetPosition(tpf);
        Vector3f oldPos = agent.getSpatial().getLocalTranslation().clone();
        Vector3f vel = calculateNewVelocity().mult(tpf);
        agent.setLocalTranslation(agent.getLocalTranslation().add(vel));
        if (agent.getLocalTranslation().x > terrainSize * 2 || agent.getLocalTranslation().z > terrainSize * 2
                || agent.getLocalTranslation().x < -terrainSize * 2 || agent.getLocalTranslation().z < -terrainSize * 2) {
            agent.setLocalTranslation(oldPos);
            // if enemy hit border make him move backwards
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //don't care about rendering
    }

    public Vector3f getSteering(){
        Vector3f desiredVelocity = targetPosition.subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
        return desiredVelocity.subtract(velocity);
    }

    public Vector3f calculateNewVelocity(){
        Vector3f steering = getSteering();
        if (steering.length()>agent.getMaxForce()) {
            steering = steering.normalize().mult(agent.getMaxForce());
        }
        agent.setAcceleration(steering.mult(1/agent.getMass()));
        velocity = velocity.add(agent.getAcceleration());
        if (velocity.length()>agent.getMoveSpeed()) {
            velocity = velocity.normalize().mult(agent.getMoveSpeed());
        }
        return velocity;
    }
    
    public void changeTargetPosition(float tpf){
        time -= tpf;
        float terrainSize = 40f;
        if (time<=0) {
            Random random = new Random();
            float x = random.nextInt() % terrainSize;
            float z = random.nextInt() % terrainSize;
            targetPosition = new Vector3f(x, 0, z);
            time = 2f;
            System.out.println(targetPosition);
        } 
    }
}
