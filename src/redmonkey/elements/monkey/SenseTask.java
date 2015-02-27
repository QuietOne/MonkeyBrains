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
public class SenseTask extends LeafTask<RMMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "tag", "number");
    public String tag;
    public int number;

    @Override
    public void run(RMMonkey redMonkey) {
        System.out.println("tag:"+tag);
        System.out.println("number:"+number);
        if (redMonkey.lookingAround(tag)) {
            success();
        } else {
            fail();
        }
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        SenseTask sense = (SenseTask) task;
        sense.tag = tag;
        return task;
    }
}
