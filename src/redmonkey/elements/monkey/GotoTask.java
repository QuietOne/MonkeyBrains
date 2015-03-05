/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

/**
 *
 */
public class GotoTask extends LeafTask<RMMonkey> {

    @Override
    public void run(RMMonkey monkey) {
        monkey.getStateMachine().changeState(RMMonkeyState.WALKING_TOWARD_BANANA);
        running();
        /*if (monkey.goTo()) {
            success();
        } else {
            fail();
        }*/
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        return task;
    }
}
