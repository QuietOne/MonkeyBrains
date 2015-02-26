/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 * Very basic sight. Can see any item that is inside a sphere with the specified radius.
 */
public class RMBasicSight extends RMSense{
    Vector3f position;
    float radius;
    public RMBasicSight(Vector3f position, float radius){
        this.position=position;
        this.radius=radius;
    }
    public ArrayList<RMItem> scan(){
        ArrayList<RMItem> result=new ArrayList<RMItem>();
        for (RMItem item:space.items){
            if (item.position.distance(position)<radius)
                result.add(item);
        }
        return result;
    }
}
