/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import redmonkey.elements.monkey.RMMonkey;

/**
 * TODO This should run every 100ms
 */
public class RedMonkeyAppState extends AbstractAppState {

    public RMSpace space = new RMSpace();
    ArrayList<RMSense> senses = new ArrayList<RMSense>();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;

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
