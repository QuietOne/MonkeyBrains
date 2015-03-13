/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 */
public class RMItem {
    public Vector3f position;
    public Vector3f direction;
    public ArrayList<String> tags=new ArrayList<String>();
    public Spatial spatial;
    public RigidBodyControl control;
    
    public RMItem(){
    }
    
    public RMItem(Spatial spatial, String... newTags){
        this.spatial=spatial;
        this.position=spatial.getWorldTranslation();
        for (String tag:newTags)
            this.tags.add(tag);
    }
    
    public boolean hasTag(String tag){
        return tags.contains(tag);
    }
    
    @Override
    public String toString(){
        String s="";
        for (String tag:tags)
            s+=(tag+",");
        s+="["+position.x+","+position.y+","+position.z+"]";
        return s;
    }
    
    //public void destroy
}
