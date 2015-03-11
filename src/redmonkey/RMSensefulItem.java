/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import redmonkey.senses.RMSense;

/**
 *
 */
public class RMSensefulItem extends RMItem{
    RMSense sense;

    public void setSense(RMSense sense){
        this.sense=sense;
    }
    public RMSense getSense(){
        return sense;
    }
}
