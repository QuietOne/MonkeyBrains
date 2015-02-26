/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey;

import java.util.ArrayList;

/**
 *
 * @author mmarcon
 */
public abstract class RMSense {
    RMSpace space;
    public abstract ArrayList<RMItem> scan();
    public void setSpace(RMSpace space){
        this.space=space;
    }

}
