/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.jme3.animation.LoopMode;
import redmonkey.RMFinishedAnim;

/**
 *
 */
public class EatTask extends LeafTask<RMMonkey> {

    RMFinishedAnim irq;
    
    public void start(RMMonkey monkey) {
            monkey.channel.setAnim("Punches", 1.f);
            monkey.channel.setLoopMode(LoopMode.DontLoop);
            irq=new RMFinishedAnim(monkey);
    }
    
    @Override
    public void run(RMMonkey monkey) {
        if (irq.testForInterrupt()){
            if (irq.didISucceed()){
                success();
                return;
            }
            else{
                fail();
                return;
            }
        }
        running();
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        EatTask eat = (EatTask) task;
        return eat;
    }
}
