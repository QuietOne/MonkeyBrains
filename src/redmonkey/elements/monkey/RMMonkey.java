/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.jme3.animation.AnimChannel;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import redmonkey.RMItem;
import redmonkey.RMOmniSight;
import redmonkey.RMSense;
import redmonkey.RMSpace;

/**
 *
 */
public class RMMonkey extends RMItem implements Telegraph{

    public RMSense sense;
    RMItem lookingFor;
    CharacterControl control;

    public RMMonkey(Vector3f position) {
        tags.add("Monkey");
        this.position = position;
    }
    AnimChannel channel;

    public void setChannel(AnimChannel channel) {
        this.channel = channel;
    }
    
    public void setCharacterControl(CharacterControl control){
        this.control=control;
    }

    public void setSpace(RMSpace space) {
        space.addItems(this);
        sense.setSpace(space);
    }
    boolean sleeping = false;
    boolean goTo = false;

    public void sleep() {
        if (!sleeping) {
            channel.setAnim("Idle");
        }
        System.out.println("zzz");
        sleeping = true;
    }
    
    public void move(Vector3f dir){
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

    public boolean goTo() {
        Vector3f goal=lookingFor.position.subtract(position);
        if (goal.length()<2)
            return true;
        move(goal.mult(0.01f));
        if (!goTo) {
            channel.setAnim("Punches");
        }
        System.out.println("GOTO");
        goTo = true;
        //TODO
        return true;
    }

    public void setLookingFor(RMItem lookingFor) {
        this.lookingFor = lookingFor;
    }

    private StateMachine<RMMonkey> stateMachine;
    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
	
    public StateMachine<RMMonkey> getStateMachine () {
        return stateMachine;
    }
}
