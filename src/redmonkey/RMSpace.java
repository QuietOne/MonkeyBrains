/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redmonkey;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
public class RMSpace {

    public HashSet<RMItem> items = new HashSet<RMItem>();
    ArrayList<RMItem> addedItems = new ArrayList<RMItem>();
    ArrayList<RMItem> removedItems = new ArrayList<RMItem>();

    public void addItems(RMItem... newItems) {
        synchronized (addedItems) {
            for (RMItem newItem : newItems) {
                addedItems.add(newItem);
                if (newItem instanceof RMSensefulItem) {
                    ((RMSensefulItem) newItem).getSense().setSpace(this);
                }
            }
        }
    }

    public void removeItems(RMItem... oldItems) {
        synchronized (removedItems) {
            for (RMItem oldItem : oldItems) {
                removedItems.remove(oldItem);
            }
        }
    }
}
