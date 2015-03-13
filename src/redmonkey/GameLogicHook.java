/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.badlogic.gdx.ai.btree.LeafTask;

/**
 *
 */
public interface GameLogicHook {
    public void endedTask(LeafTask o, RMItem... items);
}
