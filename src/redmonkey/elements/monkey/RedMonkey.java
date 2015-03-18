/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.util.ArrayList;
import redmonkey.GameLogicHook;
import redmonkey.RMItem;
import redmonkey.RMSensefulItem;

/**
 *
 */
public class RedMonkey extends RMSensefulItem {

    RMItem lookingFor;
    CharacterControl control;
    public AnimChannel channel;
    public AnimControl animControl;
    TerrainQuad terrain;
    public Node spatial=new Node();
    public Spatial model;
    Quaternion q = new Quaternion();
    float speedFact;
    GameLogicHook gameLogic;

    public RedMonkey(float x, float y,float z, TerrainQuad terrain, Spatial model, GameLogicHook gameLogic, float yTranslation) {
        tags.add("Monkey");
        this.terrain=terrain;
        this.model=model;
        this.model.setLocalTranslation(0, yTranslation, 0);
        this.spatial.move(x,y,z);
        this.position=spatial.getLocalTranslation();
        this.spatial.attachChild(model);
        this.gameLogic=gameLogic;
    }

    public void setChannel(AnimControl control) {
        this.animControl = control;
        this.channel = control.createChannel();
    }

    public void setCharacterControl(CharacterControl control) {
        this.control = control;
        this.spatial.addControl(control);
    }
    public BehaviorTree<RedMonkey> behaviorTree;

    public void setBehaviorTree(AssetManager assetManager, String tree) {
        BehaviorTreeParser<RedMonkey> parser = new BehaviorTreeParser<RedMonkey>(BehaviorTreeParser.DEBUG_NONE);
        behaviorTree = parser.parse((String) (assetManager.loadAsset(tree)), this);
    }

    public boolean notSleeping(String anim) {
        return !anim.equals(channel.getAnimationName());
    }
    
    public void stopMoving(){
        control.setWalkDirection(Vector3f.ZERO.clone());
    }
    
    public void move(Vector3f dir) {
        control.setWalkDirection(dir);
        Vector3f norM = terrain.getNormal(new Vector2f(spatial.getWorldTranslation().x, spatial.getWorldTranslation().z));
        norM = norM.cross(dir).cross(norM);
//        q.lookAt(norM, Vector3f.UNIT_Y);
        control.setViewDirection(norM);
    }

    public boolean lookingAround(ArrayList<String> tags) {
        for (RMItem sensedItem : getSense().scan()) {
            boolean hasAllTags = true;
            for (String tag : tags) {
                if (!sensedItem.hasTag(tag)) {
                    hasAllTags = false;
                }
            }
            if (hasAllTags) {
                lookingFor = sensedItem;
                return true;
            }
        }
        return false;
    }

    public boolean hasReachedLookingFor(float reachDist) {
        Vector3f goal = lookingFor.position.subtract(position);
        return goal.length() < reachDist;
    }

    Vector3f goal=new Vector3f();
    public boolean goTo() {
        goal = lookingFor.position.subtract(position,goal).normalize();
        move(goal.mult(speedFact));
        return true;
    }

    public void setLookingFor(RMItem lookingFor) {
        this.lookingFor = lookingFor;
    }
    
    public void endedTask(LeafTask o, RMItem... items){
        gameLogic.endedTask(o, items);
    }
}
