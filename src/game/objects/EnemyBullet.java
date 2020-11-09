package game.objects;

import city.cs.engine.*;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class EnemyBullet extends DynamicBody implements CollisionListener, StepListener {
    private Shape s;
    private Enemy e;
    private Player p;
    public EnemyBullet(World w, Enemy e, Player p) {
        //For the normal enemies
        super(w);
        this.e = e;
        this.p = p;
        addCollisionListener(this);
        getWorld().addStepListener(this);
        setPosition(new Vec2(e.getPosition().x, e.getPosition().y - 2.1f));

        Shape s = new PolygonShape(-0.19f,0.6f, -0.01f,0.63f, 0.17f,0.6f, 0.17f,-0.43f, 0.0f,-0.47f, -0.19f,-0.41f);

        //Different types of bullets according to 'enemyType'
        s = new BoxShape(0.3f, 0.4f);
        if ((int) e.getEnemyType() == 1) {
            s = new CircleShape(0.4f);
            addImage(new BodyImage("data/enemies/bullet1.png"));
            setLinearVelocity(new Vec2(0, -15));
        } else if ((int) e.getEnemyType() == 2) {
            addImage(new BodyImage("data/enemies/bullet2.png", 2));
            setLinearVelocity(new Vec2(0, -25));
        } else if ((int) e.getEnemyType() == 3) {
            addImage(new BodyImage("data/enemies/bullet3.png", 2));
            setLinearVelocity(new Vec2(0, -17.5f));
        } new SolidFixture(this, s);
    }

    public void Destruct() {
        //Method for destroying the bullet
        destroy();
        e.setShooting(false);
        //Repeat shooting if bullet was close to player
        float dBullet = getPosition().x - p.getPosition().x;
        float dEnemy = e.getPosition().x - p.getPosition().x;
        if (((dBullet >= -2 && dBullet <= 2) || (dEnemy >= -2 && dEnemy <= 2)) && e.getLives() > 0)
            e.Shoot();
        getWorld().removeStepListener(this);
    }

    @Override
    public void collide(CollisionEvent e) {
        //If collision is registered
        Destruct();

        //Destroy the player's bullet if collided with
        if (e.getOtherBody() instanceof PlayerBullet) ((PlayerBullet) e.getOtherBody()).Destruct();
    }

    public Enemy getEnemy() {
        return e;
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        if (p.isSpawning()) Destruct();
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        if (!((LevelManager) getWorld()).isBossInGame()) {
            //Only type 3 normal enemies OR the boss have bullets that track the player
            if ((int) e.getEnemyType() >= 3f)
                setLinearVelocity(new Vec2(p.getPosition().x - e.getPosition().x, getLinearVelocity().y));
        }
    }
}
