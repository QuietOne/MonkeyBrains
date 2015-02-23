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
    Vector3f position;
    Vector3f direction;
    ArrayList<String> tags;
    
    public boolean hasTag(String tag){
        return tags.contains(tag);
    }
}
