package com.jme3.ai.agents.util.weapons;

/**
 * Abstract class for defining weapons used by agents.
 *
 * @author Tihomir Radosavljević
 * @version 1.0.0
 */
public abstract class AbstractFirearmWeapon extends AbstractBulletBasedWeapon {
    /**
     * Number of bullet that this weapon have left. Set to -1 if infinite.
     */
    protected int numberOfBullets;
    /**
     * Check if there is any bullets in weapon.
     *
     * @return true - has, false - no, it doesn't
     */
    public boolean isUsable() {
        if (numberOfBullets == 0) {
            return false;
        }
        return true;
    }

    public int getNumberOfBullets() {
        return numberOfBullets;
    }

    public void setNumberOfBullets(int numberOfBullets) {
        this.numberOfBullets = numberOfBullets;
    }

    public void addNumberOfBullets(int numberOfBullets) {
        this.numberOfBullets += numberOfBullets;
    }

    @Override
    protected boolean isUnlimitedUse() {
        return numberOfBullets == -1;
    }

    @Override
    protected void useWeapon() {
        numberOfBullets--;
    }

}
