/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;

/**
 * TODO This should run every 100ms
 */
public class RedMonkeyAppState extends AbstractAppState{

    RMSpace space;
    ArrayList<RMSense> senses=new ArrayList<RMSense>();
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        space=new RMSpace();
    }
    
    
    public void addSense(RMSense sense){
        senses.add(sense);
    }
    
    @Override
    public void update(float tpf){
        for (RMSense sense:senses)
            sense.scan();
    }
}
