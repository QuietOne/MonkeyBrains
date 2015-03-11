/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import redmonkey.elements.monkey.interrupts.RMFinishedAnim;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Metadata;
import com.badlogic.gdx.ai.btree.Task;
import com.jme3.animation.LoopMode;

/**
 *
 */
public class EatTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "anim");
    public String anim;
    RMFinishedAnim irq;

    @Override
    public void start(RedMonkey monkey) {
        monkey.channel.setAnim(anim, 1.f);
        monkey.channel.setLoopMode(LoopMode.DontLoop);
        irq = new RMFinishedAnim(monkey);
    }

    @Override
    public void run(RedMonkey monkey) {
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
    public void end(RedMonkey monkey) {
        monkey.endedTask(this);
    }

    @Override
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        EatTask eat = (EatTask) task;
        eat.anim = anim;
        return eat;
    }
}
