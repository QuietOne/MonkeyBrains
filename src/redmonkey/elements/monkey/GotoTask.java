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
    boolean entered=false;
    
    @Override
    public void run(RMMonkey monkey) {
        if (!entered){
        //if (!monkey.getStateMachine().getCurrentState().equals(RMMonkeyState.WALKING_TOWARD_BANANA)){
        //   monkey.getStateMachine().changeState(RMMonkeyState.WALKING_TOWARD_BANANA);
            monkey.channel.setAnim("JumpStart", 1.f);
            entered=true;
            irq=new RMReachedGoalInterrupt(monkey);
        }
        monkey.goTo();
        if (irq.testForInterrupt()){
            System.out.println("Did I succeed? Reaced goal ");
            if (irq.didISucceed()){
                success();
                return;
            }
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
