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

    public RMReachedGoalInterrupt( RedMonkey monkey) {
        this.monkey=monkey;
    }
    

    
    public boolean testForInterrupt() {
        return monkey.hasReachedLookingFor();
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
