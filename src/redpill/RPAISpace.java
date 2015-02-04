/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redpill;

import java.util.ArrayList;

/**
 *
 */
public class RPAISpace {
    ArrayList<RPAIItem> items=new ArrayList<RPAIItem>();
    public void addItems(RPAIItem... newItems){
        for (RPAIItem newItem:newItems)
            items.add(newItem);
    }
    
    public void removeItems(RPAIItem... oldItems){
        for (RPAIItem oldItem:oldItems)
            items.remove(oldItem);
    }
}
