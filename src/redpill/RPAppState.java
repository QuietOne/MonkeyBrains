/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redpill;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;

/**
 * TODO This should run every 100ms
 */
public class RPAppState extends AbstractAppState{

    RPAISpace space;
    ArrayList<RPSense> senses=new ArrayList<RPSense>();
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        space=new RPAISpace();
    }
    
    
    public void addSense(RPSense sense){
        senses.add(sense);
    }
    
    @Override
    public void update(float tpf){
        for (RPSense sense:senses)
            sense.scan();
    }
}
