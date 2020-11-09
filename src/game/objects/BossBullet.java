package game.objects;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class BossBullet extends DynamicBody implements StepListener, CollisionListener {
    private EnemyBoss boss;
    private Player p;
    public BossBullet(World w, EnemyBoss boss, Player p) {
        super(w);
        this.boss = boss;
        this.p = p;
        addCollisionListener(this);
        getWorld().addStepListener(this);

        Shape s = new PolygonShape(-0.19f,0.6f, -0.01f,0.63f, 0.17f,0.6f, 0.17f,-0.43f, 0.0f,-0.47f, -0.19f,-0.41f);
        new SolidFixture(this, s);

        //Initialise initial conditions
        setPosition(new Vec2(boss.getPosition().x, boss.getPosition().y - 4.5f));
        addImage(new BodyImage("data/enemies/bossBullet.png", 3));
        setLinearVelocity(new Vec2(0, -25f));
    }

    public void Destruct() {
        //Destroy the bullet and remove steplistener
        destroy();
        boss.setShooting(false);
        getWorld().removeStepListener(this);
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        if (p.isSpawning()) Destruct();
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        //The enemy boss has bullets that track player too
        setLinearVelocity(new Vec2(p.getPosition().x - boss.getPosition().x, getLinearVelocity().y));
    }

    @Override
    public void collide(CollisionEvent e) {
        //If collision is registered
        Destruct();

        //Destroy the player's bullet if collided with
        if (e.getOtherBody() instanceof PlayerBullet) ((PlayerBullet) e.getOtherBody()).Destruct();
    }
}
