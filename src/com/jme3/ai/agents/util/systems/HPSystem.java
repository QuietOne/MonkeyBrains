package com.jme3.ai.agents.util.systems;

import com.jme3.ai.agents.Agent;

/**
 *
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class HPSystem {

    private float currentHP;
    private float maxHP;
    private Agent agent;

    public HPSystem(Agent agent) {
        this.agent = agent;
        this.currentHP = 100f;
        this.maxHP = 100f;
    }

    public HPSystem(float maxHP, Agent agent) {
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.agent = agent;
    }

    /**
     * Method for increasing agents hitPoint for fixed amount. If adding
     * hitPoint will cross the maximum hitPoint of agent, then agent's hitPoint
     * status will be set to maximum allowed hitPoint for that agent.
     *
     * @see Agent#maxHitPoint
     * @param potionHitPoint amount of hitPoint that should be added
     */
    public void increaseHP(float potionHP) {
        currentHP = Math.min(maxHP, currentHP + potionHP);
    }

    /**
     * Method for decreasing agents hitPoint for fixed amount. If hitPoint drops
     * to zero or bellow, agent's hitPoint status will be set to zero and he
     * will be dead. It is up to programmer to remove its spatial.
     *
     * @see Agent#stop()
     * @param damage amount of hitPoint that should be removed
     */
    public void decreaseHP(double damage) {
        currentHP -= damage;
        if (currentHP <= 0) {
            currentHP = 0;
            agent.stop();
        }
    }

    public float getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(float hp) {
        this.currentHP = Math.min(maxHP, hp);
    }

    public float getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(float maxHP) {
        this.maxHP = maxHP;
    }
}
