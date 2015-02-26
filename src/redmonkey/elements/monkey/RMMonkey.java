/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

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
        sense = new RMOmniSight(position);
    }

    public void setSpace(RMSpace space) {
        space.addItems(this);
        sense.setSpace(space);
    }
    
    public void sleep(){
        System.out.println("zzz");
    }

    public boolean lookingAround(String tag) {
        for (RMItem sensedItem : sense.scan()) {
            if (sensedItem.hasTag(tag)) {
                lookingFor = sensedItem;
                return true;
            }
        }
        return false;
    }

    public boolean goTo(){
        //TODO
        return true;
}
    
    public void setLookingFor(RMItem lookingFor) {
        this.lookingFor = lookingFor;
    }
}
