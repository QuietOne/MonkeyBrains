/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import redmonkey.elements.monkey.RMMonkey;

/**
 *
 */
public class RMReachedGoalInterrupt implements RMInterrupt{

    RMMonkey monkey;

    public RMReachedGoalInterrupt( RMMonkey monkey) {
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
