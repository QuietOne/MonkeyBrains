/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey.elements.monkey;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Metadata;
import com.badlogic.gdx.ai.btree.Task;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 */
public class SenseTask extends LeafTask<RedMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "tag", "number");
    public String tag;
    public ArrayList<String> tags;
    public int number;

    @Override
    public void start(RedMonkey redMonkey){
            tags=new ArrayList<String>();
        StringTokenizer st=new StringTokenizer(tag,",");
        while(st.hasMoreTokens())
            tags.add(st.nextToken());
    }
    
    @Override
    public void run(RedMonkey redMonkey) {
        if (redMonkey.lookingAround(tags)) {
            success();
            return;
        } else {
            fail();
            return;
        }
    }

    @Override
    protected Task<RedMonkey> copyTo(Task<RedMonkey> task) {
        SenseTask sense = (SenseTask) task;
        sense.tag = tag;
        sense.tags = null;
        return task;
    }
}
