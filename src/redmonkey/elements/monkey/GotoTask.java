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

    RMReachedGoalInterrupt irq;

    @Override
    public void start(RMMonkey monkey) {
        monkey.channel.setAnim("JumpStart", 1.f);
        irq = new RMReachedGoalInterrupt(monkey);
    }

    @Override
    public void run(RMMonkey monkey) {
        monkey.goTo();
        if (irq.testForInterrupt()) {
            if (irq.didISucceed()) {
                success();
                return;
            } else {
                fail();
                return;
            }
        }
        running();
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        return task;
    }
}
