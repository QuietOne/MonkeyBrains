/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import java.util.HashSet;

/**
 *
 */
public class RMSpace {
    public HashSet<RMItem> items=new HashSet<RMItem>();
    public void addItems(RMItem... newItems){
        for (RMItem newItem:newItems){
            items.add(newItem);
             if (newItem instanceof RMSensefulItem)
                 ((RMSensefulItem)newItem).getSense().setSpace(this);
        }
    }
    
    public void removeItems(RMItem... oldItems){
        for (RMItem oldItem:oldItems)
            items.remove(oldItem);
    }
}
