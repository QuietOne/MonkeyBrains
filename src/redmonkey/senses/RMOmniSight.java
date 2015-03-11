/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey.senses;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import redmonkey.RMItem;

/**
 * Very basic sight. Can see everything.
 */
public class RMOmniSight extends RMSense{
    public RMOmniSight(){
    }
    
    public ArrayList<RMItem> scan(){
        ArrayList<RMItem> result=new ArrayList<RMItem>();
        for (RMItem item:space.items){
            result.add(item);
        }
        return result;
    }
}
