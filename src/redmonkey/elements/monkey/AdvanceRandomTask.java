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
public class AdvanceRandomTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "min", "max");
    public int min;
    public int max;

    @Override
    public void start(RedMonkey redMonkey){
    }
    
    @Override
    public void run(RedMonkey redMonkey) {
        float v=(float)Math.random();
        v=((max-min)*v)+min;
        redMonkey.advanceGround(v);
        success();
    }

    @Override
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        AdvanceRandomTask sense = (AdvanceRandomTask) task;
        sense.min = min;
        sense.max = max;
        return task;
    }
}
