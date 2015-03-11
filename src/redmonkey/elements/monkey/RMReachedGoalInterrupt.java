/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey.elements.monkey;

import redmonkey.RMInterrupt;
import redmonkey.elements.monkey.RedMonkey;

/**
 *
 */
public class RMReachedGoalInterrupt implements RMInterrupt{

    RedMonkey monkey;
    float reachDist;

    public RMReachedGoalInterrupt( RedMonkey monkey, float reachDist) {
        this.monkey=monkey;
        this.reachDist=reachDist;
    }
    

    
    public boolean testForInterrupt() {
        return monkey.hasReachedLookingFor(reachDist);
    }

    
    //non fa nulla
    public boolean manageInterrupt() {
        return true;
    }

    
    //va chiamato SOLO SE interrotto, quindi se e' arrivato
    public boolean didISucceed() {
        return true;
    }

}
