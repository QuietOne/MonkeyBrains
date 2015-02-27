/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 */
public class RMItem {
    public Vector3f position;
    public Vector3f direction;
    public ArrayList<String> tags=new ArrayList<String>();
    
    public boolean hasTag(String tag){
        return tags.contains(tag);
    }
}
