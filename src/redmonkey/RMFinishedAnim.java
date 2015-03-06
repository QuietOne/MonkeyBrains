/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import redmonkey.elements.monkey.RMMonkey;

/**
 *
 */
public class RMFinishedAnim implements RMInterrupt, AnimEventListener{

    RMMonkey monkey;

    public RMFinishedAnim( RMMonkey monkey) {
        this.monkey=monkey;
        monkey.animControl.addListener(this);
    }
    

    
    public boolean testForInterrupt() {
        return !isFinishedAnim;
    }

    
    //non fa nulla
    public boolean manageInterrupt() {
        return true;
    }

    
    //va chiamato SOLO SE interrotto, quindi se e' arrivato
    public boolean didISucceed() {
        return true;
    }

    boolean isFinishedAnim;
    
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        isFinishedAnim=true;
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

}
