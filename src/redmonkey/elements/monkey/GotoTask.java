/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import redmonkey.elements.monkey.interrupts.RMReachedGoalInterrupt;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Metadata;
import com.badlogic.gdx.ai.btree.Task;
import java.util.ArrayList;

/**
 *
 */
public class GotoTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "anim", "speedFact", "reachDist");
    public String anim;
    public float speedFact;
    public float reachDist;
    RMReachedGoalInterrupt irq;

    @Override
    public void start(RedMonkey monkey) {
        if (anim!=null)
        monkey.channel.setAnim(anim, 1.f);
        monkey.speedFact=speedFact;
        irq = new RMReachedGoalInterrupt(monkey,reachDist);
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
    public void end(RedMonkey monkey) {
        monkey.endedTask(this, monkey.lookingFor);
    }

    @Override
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        GotoTask sense = (GotoTask) task;
        sense.anim = anim;
        sense.speedFact = speedFact;
        sense.reachDist = reachDist;
        return task;
    }
}
