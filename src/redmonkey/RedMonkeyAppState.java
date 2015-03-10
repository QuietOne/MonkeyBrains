/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.scene.Node;
import java.util.ArrayList;
import redmonkey.elements.monkey.RMMonkey;

/**
 * TODO This should run every 100ms
 */
public class RedMonkeyAppState extends AbstractAppState {

    public RMSpace space = new RMSpace();
    ArrayList<RMSense> senses = new ArrayList<RMSense>();
    protected boolean debugEnabled = false;
    protected RedMonkeyDebugAppState debugAppState;
    
    Node rootNode;
    BitmapFont guiFont;
    
    public RedMonkeyAppState(Node rootNode, BitmapFont guiFont){
        this.rootNode=rootNode;
        this.guiFont=guiFont;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        if (debugEnabled) {
            debugAppState = new RedMonkeyDebugAppState(space, rootNode, guiFont);
            stateManager.attach(debugAppState);
        }
    }
    
    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
    
    public RMSpace getSpace() {
        return space;
    }

    public void addSense(RMSense sense) {
        senses.add(sense);
    }

    @Override
    public void update(float tpf) {
        for (RMSense sense : senses) {
            sense.scan();
        }
        for (RMItem item : space.items) {
            if (item instanceof RMMonkey) {
                ((RMMonkey) item).behaviorTree.step();
            }
        }
    }
}
