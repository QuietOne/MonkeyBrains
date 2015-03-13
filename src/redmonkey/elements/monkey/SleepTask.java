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
public class SleepTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "anim");
    public String anim;
    public int times = 1;

    @Override
    public void start(RedMonkey monkey) {
        monkey.channel.setAnim(anim, 1.f);
    }

    @Override
    public void run(RedMonkey monkey) {
        for (int i = 0; i < times; i++) {
            monkey.sleep();
        }
        success();
    }

    @Override
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        SleepTask sleep = (SleepTask) task;
        sleep.anim = anim;

        return task;
    }
}
