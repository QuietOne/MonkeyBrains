package com.jme3.ai.agents.behaviours.npc.steering;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Purpose of seek behaviour is to steer agent towards a specified position or
 * object. Basically it's movement behaviour. For your use of seek behaviour you
 * only need to override controlUpdate() and controlRender() and use appropriate 
 * listeners. For now it hasn't been done, because this is in demo version.
 * @author Tihomir Radosavljević
 */
public class SeekBehaviour extends Behaviour{

    private Agent target;
    private Vector3f velocity;
    
    public SeekBehaviour(Agent agent, Agent target) {
        super(agent);
        this.target = target;
        velocity = new Vector3f();
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        float terrainSize = 40f;
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
        Vector3f desiredVelocity = target.getLocalTranslation().subtract(agent.getLocalTranslation()).normalize().mult(agent.getMoveSpeed());
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
    
    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Agent getTarget() {
        return target;
    }

    public void setTarget(Agent target) {
        this.target = target;
    }
    
}
