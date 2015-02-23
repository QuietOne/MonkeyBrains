/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import java.util.ArrayList;

/**
 *
 */
public class RMSpace {
    ArrayList<RMItem> items=new ArrayList<RMItem>();
    public void addItems(RMItem... newItems){
        for (RMItem newItem:newItems)
            items.add(newItem);
    }
    
    public void removeItems(RMItem... oldItems){
        for (RMItem oldItem:oldItems)
            items.remove(oldItem);
    }
}
