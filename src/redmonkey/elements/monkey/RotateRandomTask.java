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
public class RotateRandomTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "degY");
    public int degY;

    @Override
    public void start(RedMonkey redMonkey){
    }
    
    @Override
    public void run(RedMonkey redMonkey) {
        float v=(float)Math.random();
        v=-(v/2);
        redMonkey.rotateY((float) Math.toRadians(v*degY));
        success();
    }

    @Override
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        RotateRandomTask sense = (RotateRandomTask) task;
        sense.degY = degY;
        return task;
    }
}
