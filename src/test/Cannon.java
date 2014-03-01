package test;

import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ai.agents.util.AbstractWeapon;
import com.jme3.ai.agents.util.Bullet;
import com.jme3.ai.agents.util.Game;

/**
 *
 * @author Tihomir Radosavljević
 */
public class Cannon extends AbstractWeapon{

     public Cannon(String name, Agent agent, float attackRange, float attackDamage){
        this.name = name;
        this.agent = agent;
        this.maxAttackRange = attackRange;
        this.attackDamage = attackDamage;
        this.numberOfBullets = -1;
    }
    
    @Override
    protected Bullet controlShootAt(Agent target, float tpf) {
        Bullet cannonBall = new CannonBall(this, DefinedSpatials.initializeCannonball(), target.getLocalTranslation());
        bullet = cannonBall;
        ((Node) Game.getInstance().getRootNode()).attachChild(cannonBall.getSpatial());
        return cannonBall;
    }

    @Override
    protected Bullet controlShootAt(Vector3f direction, float tpf) {
        Bullet cannonBall = new CannonBall(this, DefinedSpatials.initializeCannonball(), direction);
        bullet = cannonBall;
        ((Node) Game.getInstance().getRootNode()).attachChild(cannonBall.getSpatial());
        return cannonBall;
    }

    
}
