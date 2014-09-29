package com.jme3.ai.agents.util.systems;

import com.jme3.ai.agents.util.weapons.AbstractWeapon;

/**
 * InventorySystem is meant for making custom inventory that Agent will use.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public interface InventorySystem {

    public void update(float tpf);

    public float getInventoryMass();

    public AbstractWeapon getActiveWeapon();
}
