package game.objects;

import city.cs.engine.*;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class EnemyBoss extends Walker implements StepListener, CollisionListener {
    private Player p;
    private boolean spawning = false;
    private int iniY;
    private static int lives = 25;
    private int enemyType = 4;
    private boolean isShooting = false;
    private boolean isMovingRight = true;
    public EnemyBoss(World w, Player p, int iniY) {
        super(w);
        this.p = p;
        this.iniY = iniY;
        w.addStepListener(this);
        addCollisionListener(this);

        //Creates shape and fixture
        Shape s = new PolygonShape(
                8.12f,-1.64f, 8.2f,-0.24f, 3.12f,3.64f, -3.16f,3.72f, -8.24f,-0.2f, -8.2f,-1.72f, -5.2f,-3.64f, 5.08f,-3.64f);
        Fixture sf = new SolidFixture(this, s);
        addImage(new BodyImage("data/enemies/enemyBoss.png", 20));
        setPosition(new Vec2(0, iniY));
        ((LevelManager) getWorld()).setBossInGame(true);

        Spawn();
    }

    public void Spawn() {
        //Begin animation for spawn
        spawning = true;
        setLinearVelocity(new Vec2(0, -5));
        ((LevelManager) getWorld()).setEnemiesSpawning(true);
    }

    public void Shoot() {
        //Shoot a boss bullet
        isShooting = true;
        new BossBullet(getWorld(), this, p);
    }

    public void Destruct() {
        destroy();
        new EnemyExplode(this, 10);
        //Remove the boss's health from JPanel
        LevelManager.getGame().getControlPanel().ResetDisplay();
        ((LevelManager) getWorld()).setLevelCompleted(true);
        System.out.println("Game Complete!");
    }

    public boolean isSpawning() {
        return spawning;
    }

    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        EnemyBoss.lives = lives;
    }

    @Override
    public void collide(CollisionEvent e) {
        setLinearVelocity(new Vec2(getLinearVelocity().x, 0));

        //If boss collides with player's bullet
        if (e.getOtherBody() instanceof PlayerBullet) {
            ((PlayerBullet) e.getOtherBody()).Destruct();
            lives--;
            LevelManager.getGame().getControlPanel().getBossHealth().setValue(lives);
            if (lives <= 0) {
                Destruct();
                getWorld().removeStepListener(this);
            }
        }
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        //Keep shooting at player
        if (!isSpawning() && !isShooting) Shoot();
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        //Spawn animation and moving right/left
        if (spawning && getPosition().y < iniY - 18.5f) {
            setLinearVelocity(new Vec2(0, 0));
            spawning = false;
            ((LevelManager) getWorld()).setEnemiesSpawning(false);
        } if (!spawning && isMovingRight) {
            setLinearVelocity(new Vec2(6, 0));
            if (getPosition().x > 13) isMovingRight = false;
        } else if (!isMovingRight) {
            setLinearVelocity(new Vec2(-6, 0));
            if (getPosition().x < -13) isMovingRight = true;
        }
    }
}
