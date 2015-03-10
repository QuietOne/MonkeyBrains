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
public class SenseTask extends LeafTask<RMMonkey> {

    public static final Metadata METADATA = new Metadata(LeafTask.METADATA, "tag", "number");
    public String tag;
    public ArrayList<String> tags;
    public int number;

    @Override
    public void start(RMMonkey redMonkey){
            tags=new ArrayList<String>();
        System.out.println("tag:"+tag);
        StringTokenizer st=new StringTokenizer(tag,",");
        while(st.hasMoreTokens())
            tags.add(st.nextToken());
    }
    
    @Override
    public void run(RMMonkey redMonkey) {
        if (redMonkey.lookingAround(tags)) {
            success();
            return;
        } else {
            fail();
            return;
        }
    }

    @Override
    protected Task<RMMonkey> copyTo(Task<RMMonkey> task) {
        SenseTask sense = (SenseTask) task;
        sense.tag = tag;
        sense.tags = null;
        return task;
    }
}
