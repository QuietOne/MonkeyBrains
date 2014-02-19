package test;

import agents.Agent;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import util.AbstractWeapon;
import util.Bullet;
import util.Game;

/**
 *
 * @author Tihomir Radosavljević
 */
public class CannonBall extends Bullet{
    
    private Vector3f direction;
    
    public CannonBall(AbstractWeapon weapon, Spatial spatial, Vector3f direction) {
        super(weapon, spatial);
        speed = 20f;
        this.direction = direction;
        spatial.setLocalRotation(weapon.getAgent().getLocalRotation());
        spatial.setLocalTranslation(weapon.getAgent().getLocalTranslation());
        spatial.getLocalTranslation().y = 3;
    }

    @Override
    public void update(float tpf) {
        Game game = Game.getInstance();
        if (weapon.getAgent().getLocalTranslation().distance(spatial.getLocalTranslation())
                >weapon.getMaxAttackRange()) {
            weapon.setBullet(null);
            game.removeBullet(this);
            return;
        }
        Vector3f click3d = new Vector3f(weapon.getAgent().getLocalTranslation());
        Vector3f dir = direction.subtract(click3d).normalizeLocal();
        spatial.move(dir.x*speed * tpf, 0, dir.z*speed * tpf);
        //this the part where it hurts
        for (Agent target : game.getAgents()) {
            if (hurts(target) && !weapon.getAgent().equals(target)) {
                game.decreaseHealth(target, weapon.getAgent().getWeapon().getAttackDamage());
                ((Quad) ((Geometry) ((Node) target.getSpatial()).getChild("healthbar")).getMesh()).updateGeometry(target.getHealth() / 100 * 4, 0.2f);
                weapon.setBullet(null);
                game.removeBullet(this);
            }
        }
    }

    private boolean hurts(Agent agent){
        if (spatial.getLocalTranslation().distance(agent.getLocalTranslation())< 5 && agent.isAlive()) {
            return true;
        }
        return false;
    }
    
}
