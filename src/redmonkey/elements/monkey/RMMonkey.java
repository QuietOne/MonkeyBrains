/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.jme3.animation.AnimChannel;
import com.jme3.math.Vector3f;
import redmonkey.RMItem;
import redmonkey.RMOmniSight;
import redmonkey.RMSense;
import redmonkey.RMSpace;

/**
 *
 */
public class RMMonkey extends RMItem {

    public RMSense sense;
    RMItem lookingFor;

    public RMMonkey(Vector3f position) {
        tags.add("Monkey");
        this.position = position;
    }

    AnimChannel channel;
    
    public void setChannel(AnimChannel channel){
        this.channel=channel;
    }
    
    public void setSpace(RMSpace space) {
        space.addItems(this);
        sense.setSpace(space);
    }
    
    boolean sleeping=false;
    boolean goTo=false;
    
    public void sleep(){
        if (!sleeping)
            channel.setAnim("Idle");
        System.out.println("zzz");
        sleeping=true;
    }

    public boolean lookingAround(String tag) {
        System.out.print("looking around");
        for (RMItem sensedItem : sense.scan()) {
            if (sensedItem.hasTag(tag)) {
                lookingFor = sensedItem;
                return true;
            }
        }
        return false;
    }

    public boolean goTo(){
        if (!goTo)
            channel.setAnim("Punches");
        System.out.println("GOTO");
        goTo=true;
        //TODO
        return true;
}
    
    public void setLookingFor(RMItem lookingFor) {
        this.lookingFor = lookingFor;
    }
}
