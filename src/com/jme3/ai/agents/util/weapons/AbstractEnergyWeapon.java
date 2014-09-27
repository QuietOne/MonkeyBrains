package com.jme3.ai.agents.util.weapons;

/**
 *
 * @author Pesegato
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public abstract class AbstractEnergyWeapon extends AbstractBulletBasedWeapon {

    /**
     * Amount of energy needed to charge the weapon. Set to -1 if infinite.
     */
    protected int energyRequired;
    /**
     * The power source that fuels the weapon.
     */
    protected AbstractPowerSource powerSource;

    @Override
    public boolean isUsable() {
        return powerSource.contains(energyRequired);
    }

    @Override
    protected boolean isUnlimitedUse() {
        return energyRequired == -1;
    }

    @Override
    protected void useWeapon() {
        powerSource.consume(energyRequired);
    }

    public int getEnergyRequired() {
        return energyRequired;
    }

    public void setEnergyRequired(int energyRequired) {
        this.energyRequired = energyRequired;
    }

    public AbstractPowerSource getPowerSource() {
        return powerSource;
    }

    public void setPowerSource(AbstractPowerSource powerSource) {
        this.powerSource = powerSource;
    }
}
