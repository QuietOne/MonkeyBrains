/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import redmonkey.RMItem;
import redmonkey.RMSense;
import redmonkey.RMSpace;

/**
 *
 */
public class RMMonkey extends RMItem {

    public RMSense sense;
    RMItem lookingFor;
    CharacterControl control;

    public RMMonkey(Vector3f position) {
        tags.add("Monkey");
        this.position = position;
    }
    AnimChannel channel;
    public AnimControl animControl;

    public void setChannel(AnimControl control) {
        this.animControl = control;
        this.channel = control.createChannel();
    }

    public void setCharacterControl(CharacterControl control) {
        this.control = control;
    }
    public BehaviorTree<RMMonkey> behaviorTree;

    public void setBehaviorTree(AssetManager assetManager, String tree) {
        BehaviorTreeParser<RMMonkey> parser = new BehaviorTreeParser<RMMonkey>(BehaviorTreeParser.DEBUG_NONE);
        behaviorTree = parser.parse((String) (assetManager.loadAsset(tree)), this);
    }

    public void setSpace(RMSpace space) {
        space.addItems(this);
        sense.setSpace(space);
    }
    boolean goTo = false;

    public void sleep() {
        System.out.println("zzz: check for irq?");
    }

    public void move(Vector3f dir) {
        control.setWalkDirection(dir);
    }

    public boolean lookingAround(ArrayList<String> tags) {
        System.out.print("looking around");
        for (RMItem sensedItem : sense.scan()) {
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

    public boolean hasReachedLookingFor() {
        Vector3f goal = lookingFor.position.subtract(position);
        return goal.length() < 2;
    }

    public boolean goTo() {
        Vector3f goal = lookingFor.position.subtract(position).normalize();
        move(goal.mult(0.01f));
        return true;
    }

    public void setLookingFor(RMItem lookingFor) {
        this.lookingFor = lookingFor;
    }
}
