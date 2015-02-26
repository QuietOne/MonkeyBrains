/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey.elements;

import com.jme3.math.Vector3f;
import redmonkey.RMItem;

/**
 *
 */
public class RMBanana extends RMItem{
    public RMBanana(Vector3f position){
        tags.add("Banana");
        this.position=position;
    }

}
