/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey;

import redmonkey.senses.RMSense;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import redmonkey.elements.monkey.RedMonkey;

/**
 * TODO This should run every 100ms
 */
public class RedMonkeyAppState extends AbstractAppState {

    public RMSpace space = new RMSpace();
    ArrayList<RMSense> senses = new ArrayList<RMSense>();
    protected boolean debugEnabled = false;
    protected RedMonkeyDebugAppState debugAppState;
    
    BitmapFont guiFont;
    
    public RedMonkeyAppState(BitmapFont guiFont){
        this.guiFont=guiFont;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        app.getAssetManager().registerLoader(RMLoader.class, "redmonkey");
        initialized = true;
        if (debugEnabled) {
            debugAppState = new RedMonkeyDebugAppState(space, guiFont);
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
        try{
        for (RMItem item : space.items) {
            if (item instanceof RedMonkey) {
                ((RedMonkey) item).behaviorTree.step();
            }
        }}catch(ConcurrentModificationException e){e.printStackTrace();}
    }

    
    public enum ThreadingType {

        /**
         * Default mode; user update, ai update and rendering happen
         * sequentially (single threaded)
         */
        SEQUENTIAL,
        /**
         * Parallel threaded mode; ai update and rendering are executed in
         * parallel, update order is kept.<br/> Multiple RedMonkeyAppStates will
         * execute in parallel in this mode.
         */
        PARALLEL,
    }
}
