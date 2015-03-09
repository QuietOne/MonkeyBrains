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
        boolean b=monkey.hasReachedLookingFor();
        System.out.println("tfi? "+b);
        return b;
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
