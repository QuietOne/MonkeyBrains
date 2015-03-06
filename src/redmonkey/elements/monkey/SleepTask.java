/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Metadata;
import com.badlogic.gdx.ai.btree.Task;

/**
 *
 */
public class SleepTask extends LeafTask<RMMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "times");
    public int times = 1;

    @Override
    public void run(RMMonkey monkey) {
        monkey.getStateMachine().changeState(RMMonkeyState.IDLE);
        for (int i = 0; i < times; i++) {
            monkey.sleep();
        }
        success();
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        SleepTask sleep = (SleepTask) task;
        sleep.times = times;

        return task;
    }
}
