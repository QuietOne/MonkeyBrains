/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import redmonkey.RMReachedGoalInterrupt;

/**
 *
 */
public class GotoTask extends LeafTask<RMMonkey> {

    RMReachedGoalInterrupt irq;
    
    @Override
    public void run(RMMonkey monkey) {
        if (!monkey.getStateMachine().getCurrentState().equals(RMMonkeyState.WALKING_TOWARD_BANANA)){
            monkey.getStateMachine().changeState(RMMonkeyState.WALKING_TOWARD_BANANA);
            irq=new RMReachedGoalInterrupt(monkey);
        }
        monkey.goTo();
        if (irq.testForInterrupt()){
            if (irq.didISucceed())
                System.exit(0);
            else
                fail();
        }
        running();
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        return task;
    }
}
