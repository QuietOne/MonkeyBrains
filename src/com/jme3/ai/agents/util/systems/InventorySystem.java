package com.jme3.ai.agents.util.systems;

import com.jme3.ai.agents.util.GameObject;
import com.jme3.ai.agents.util.weapons.AbstractWeapon;

/**
 * InventorySystem is meant for making custom inventory that Agent will use.
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public interface InventorySystem {

    public void addItem(GameObject gameObject);

    public void getItem(GameObject gameObject);
    
    public void update(float tpf);
    
    public float getInventoryMass();
    
    public AbstractWeapon getActiveWeapon();
    
    /*
    /**
     * It will add weapon to agent and add its spatial to agent, if there
     * already was weapon before with its own spatial, it will remove it before
     * adding new weapon spatial.
     *
     * @param weapon that agent will use
     *
    public void setActiveWeapon(AbstractWeapon weapon) {
        //remove previous weapon spatial
        if (this.weapon != null && this.weapon.getSpatial() != null) {
            this.weapon.getSpatial().removeFromParent();
        }
        //add new weapon spatial if there is any
        if (weapon.getSpatial() != null) {
            ((Node) spatial).attachChild(weapon.getSpatial());
        }
        this.weapon = weapon;
    }*/
}
