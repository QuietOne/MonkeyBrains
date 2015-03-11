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
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.util.ArrayList;
import redmonkey.GameLogicHook;
import redmonkey.RMItem;
import redmonkey.RMSensefulItem;
import redmonkey.senses.RMSense;
import redmonkey.RMSpace;

/**
 *
 */
public class RedMonkey extends RMSensefulItem {

    RMItem lookingFor;
    CharacterControl control;
    AnimChannel channel;
    public AnimControl animControl;
    TerrainQuad terrain;
    Spatial spatial;
    Quaternion q = new Quaternion();
    float speedFact;
    GameLogicHook gameLogic;

    public RedMonkey(Vector3f position, TerrainQuad terrain, Spatial spatial, GameLogicHook gameLogic) {
        tags.add("Monkey");
        this.position = position;
        this.terrain=terrain;
        this.spatial=spatial;
        this.gameLogic=gameLogic;
    }

    public void setChannel(AnimControl control) {
        this.animControl = control;
        this.channel = control.createChannel();
    }

    public void setCharacterControl(CharacterControl control) {
        this.control = control;
    }
    public BehaviorTree<RedMonkey> behaviorTree;

    public void setBehaviorTree(AssetManager assetManager, String tree) {
        BehaviorTreeParser<RedMonkey> parser = new BehaviorTreeParser<RedMonkey>(BehaviorTreeParser.DEBUG_NONE);
        behaviorTree = parser.parse((String) (assetManager.loadAsset(tree)), this);
    }

    public void setSpace(RMSpace space) {
        space.addItems(this);
        getSense().setSpace(space);
    }

    public void sleep() {
        System.out.println("zzz: check for irq?");
    }

    public void move(Vector3f dir) {
        control.setWalkDirection(dir);
        Vector3f norM = terrain.getNormal(new Vector2f(spatial.getWorldTranslation().x, spatial.getWorldTranslation().z));
        norM = norM.cross(dir).cross(norM);
        q.lookAt(norM, Vector3f.UNIT_Y);
        spatial.setLocalRotation(q);
    }

    public boolean lookingAround(ArrayList<String> tags) {
        System.out.println("looking around");
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

    public boolean goTo() {
        Vector3f goal = lookingFor.position.subtract(position).normalize();
        move(goal.mult(speedFact));
        return true;
    }

    public void setLookingFor(RMItem lookingFor) {
        this.lookingFor = lookingFor;
    }
    
    public void endedTask(LeafTask o){
        gameLogic.endedTask(o);
    }
}
