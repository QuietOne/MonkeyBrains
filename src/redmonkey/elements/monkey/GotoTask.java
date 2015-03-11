/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Metadata;
import com.badlogic.gdx.ai.btree.Task;
import java.util.ArrayList;

/**
 *
 */
public class GotoTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "anim", "speedFact");
    public String anim;
    public float speedFact;
    RMReachedGoalInterrupt irq;

    @Override
    public void start(RedMonkey monkey) {
        monkey.channel.setAnim(anim, 1.f);
        monkey.speedFact=speedFact;
        irq = new RMReachedGoalInterrupt(monkey);
    }

    @Override
    public void run(RedMonkey monkey) {
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
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        GotoTask sense = (GotoTask) task;
        sense.anim = anim;
        sense.speedFact = speedFact;
        return task;
    }
}
