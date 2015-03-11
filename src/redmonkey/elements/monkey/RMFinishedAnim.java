/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey.elements.monkey;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import redmonkey.RMInterrupt;
import redmonkey.elements.monkey.RedMonkey;

/**
 *
 */
public class RMFinishedAnim implements RMInterrupt, AnimEventListener{

    RedMonkey monkey;

    public RMFinishedAnim( RedMonkey monkey) {
        this.monkey=monkey;
        monkey.animControl.addListener(this);
    }
    

    
    public boolean testForInterrupt() {
        return isFinishedAnim;
    }

    
    //non fa nulla
    public boolean manageInterrupt() {
        return true;
    }

    
    //va chiamato SOLO SE interrotto, quindi se e' arrivato
    public boolean didISucceed() {
        return true;
    }

    boolean isFinishedAnim=false;
    
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        isFinishedAnim=true;
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

}
