/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redpill;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 * Very basic sight. Can see everything.
 */
public class RPOmniSight implements RPSense{
    Vector3f position;
    RPAISpace space;
    float radius;
    public RPOmniSight(RPAISpace space, Vector3f position, float radius){
        this.position=position;
        this.space=space;
        this.radius=radius;
    }
    public ArrayList<RPAIItem> scan(){
        ArrayList<RPAIItem> result=new ArrayList<RPAIItem>();
        for (RPAIItem item:space.items){
            result.add(item);
        }
        return result;
    }
}
