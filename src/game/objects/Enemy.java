package game.objects;

import city.cs.engine.*;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Enemy extends Walker implements StepListener, CollisionListener {
    private LevelManager w;
    private Shape s;
    private int lives;
    private Player p;
    private float enemyType;
    private int iPos;
    private int jPos;
    private static Enemy[][] enemyList;
    private boolean spawning = false;
    private boolean moveDown = false;
    private float iniX;
    private float iniY;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = true;
    private boolean isShooting = false;
    private EnemyBullet bullet;
    private int iSize;
    /**
     * This method instantiates an enemy body.
     * <p>
     *     Use of this is in all levels, see parameters for how they're instantiated.
     *     </br>
     *     All enemies can shoot, but not all can move.
     *     </br>
     *     Only type 3 enemies have bullets that follow {@link Player}.
     *     </br>
     *     All enemies are stored into a 2D Array.
     *     </br>
     *     Enemies use {@link FireZone} to shoot.
     * </p>
     * @param w Current World enemy is instantiating in.
     * @param p Player Body that the enemy is facing.
     * @param enemyType Type of enemy.
     * @param iPos Row index in 2D enemyList array.
     * @param jPos Column index in 2D enemyList array.
     * @param enemyList 2D Array holding all enemies.
     * @param iniX Initial x position.
     * @param iniY Initial y position.
     */
    public Enemy(World w, Player p, float enemyType, int iPos, int jPos, Enemy[][] enemyList, float iniX, float iniY) {
        super(w);
        w.addStepListener(this);
        addCollisionListener(this);
        setPosition(new Vec2(iniX, iniY));
        this.w = ((LevelManager) w);

        //Creates shape and fixture
        if ((int) enemyType == 1)
            s = new PolygonShape(-0.93f, 1.06f, 0.92f, 1.05f, 1.39f, 0.02f, 1.39f, -0.78f, 0.66f, -1.03f, -0.65f, -1.04f, -1.4f, -0.78f, -1.41f, 0.04f);
        else if ((int) enemyType == 2)
            s = new PolygonShape(1.55f,-1.02f, 1.55f,0.5f, 1.29f,0.77f, 0.5f,1.03f, -0.5f,1.03f, -1.29f,0.77f, -1.54f,0.5f, -1.56f,-1.02f);
        else if ((int) enemyType == 3)
            s = new PolygonShape(-1.54f,-1.53f, 1.53f,-1.54f, 1.53f,0.35f, 0.35f,1.54f, -0.34f,1.54f, -1.53f,0.34f);
        Fixture sf = new SolidFixture(this, s);

        this.p = p;
        this.enemyType = enemyType;
        this.iPos = iPos;
        this.jPos = jPos;
        Enemy.enemyList = enemyList;
        this.iniX = iniX;
        this.iniY = iniY;

        //Apply image to enemy relevant to their 'enemyType'
        if (enemyType == 1.1f) addImage(new BodyImage("data/enemies/enemy1.png", 4));
        else if (enemyType == 1.2f) addImage(new BodyImage("data/enemies/enemy1E.png", 4));
        else if (enemyType == 1.3f) addImage(new BodyImage("data/enemies/enemy1H.png", 4));
        else if (enemyType == 2.1f) addImage(new BodyImage("data/enemies/enemy2.png", 4));
        else if (enemyType == 2.2f) addImage(new BodyImage("data/enemies/enemy2E.png", 4));
        else if (enemyType == 2.3f) addImage(new BodyImage("data/enemies/enemy2H.png", 4));
        else if (enemyType == 3.1f) addImage(new BodyImage("data/enemies/enemy3.png", 4));
        else if (enemyType == 3.2f) addImage(new BodyImage("data/enemies/enemy3E.png", 4));
        else if (enemyType == 3.3f) addImage(new BodyImage("data/enemies/enemy3H.png", 4));

        //Apply health to enemy according to 'enemyType'
        if (enemyType == 1.1f || enemyType == 2.1f || enemyType == 3.1f) lives = 1;
        else if (enemyType == 1.2f || enemyType == 2.2f || enemyType == 3.2f) lives = 2;
        else if (enemyType == 1.3f || enemyType == 2.3f || enemyType == 3.3f) lives = 3;

        //Bug fix for level 4
        if (this.w.getLevel() < 4) iSize = 4;
        if (this.w.getLevel() == 4) iSize = 2;

        Spawn();
    }

    /**
     * Spawns enemy into game.
     * <p>
     *     Typical use of this method is when an {@link Enemy} is instantiated and needs to move into position.
     * </p>
     */
    public void Spawn() {
        //Begin animation for spawn
        spawning = true;
        setLinearVelocity(new Vec2(0, -5));
    }

    /**
     * Shoots a bullet from enemy if they're in the bottom row of their column.
     * <p>
     *     Typical use of this is when {@link Player} is in range.
     *     </br>
     *     Range is determined by {@link InvisibleBorder}s in {@link FireZone}.
     * </p>
     */
    public void Shoot() {
        //If there is no enemy directly below, shoots a bullet
        if (!isShooting && (iPos == iSize || CheckDown()) && !w.isLevelCompleted()) {
            isShooting = true;
            bullet = new EnemyBullet(getWorld(), this, p);
        }
    }

    /**
     * Method scans for enemies below.
     * <p>
     *     Returns true if there are no enemies directly below an enemy.
     * </p>
     * @return true/false.
     */
    public boolean CheckDown() {
        //Check if there any enemy directly below
        int iFactor = iSize - iPos;
        int count = 0;
        for (int i=1; i<=iFactor; i++)
            if (enemyList[iPos+i][jPos] == null) count++;
        if (count == iFactor) return true;
        else return false;
    }

    /**
     * This method changes the appearance of the Enemy's sprite.
     * <p>
     *     Typical of this is when the enemy is shot, or when loading the enemies previous state.
     * </p>
     */
    public void UpdateSprite() {
        //Change enemy colour depending on how many lives they have left
        removeAllImages();
        switch (lives) {
            case 1:
                if ((int) enemyType == 1) addImage(new BodyImage("data/enemies/enemy1.png", 4));
                else if ((int) enemyType == 2) addImage(new BodyImage("data/enemies/enemy2.png", 4));
                else if ((int) enemyType == 3) addImage(new BodyImage("data/enemies/enemy3.png", 4));
                break;
            case 2:
                if ((int) enemyType == 1) addImage(new BodyImage("data/enemies/enemy1E.png", 4));
                else if ((int) enemyType == 2) addImage(new BodyImage("data/enemies/enemy2E.png", 4));
                else if ((int) enemyType == 3) addImage(new BodyImage("data/enemies/enemy3E.png", 4));
                break;
            case 3:
                if ((int) enemyType == 1) addImage(new BodyImage("data/enemies/enemy1H.png", 4));
                else if ((int) enemyType == 2) addImage(new BodyImage("data/enemies/enemy2H.png", 4));
                else if ((int) enemyType == 3) addImage(new BodyImage("data/enemies/enemy3H.png", 4));
                break;
        }
    }

    /**
     * Destroys the enemy body and rewards player score depending on enemyType.
     * <p>
     *     60% chance of dropping a coin.
     *     </br>
     *     10% chance of dropping a star.
     * </p>
     */
    public void Destroy () {
        //Remove stepListener and update variables
        getWorld().removeStepListener(this);
        enemyList[iPos][jPos] = null;
        p.setEnemiesList(enemyList);
        w.setEnemiesLeft(w.getEnemiesLeft() - 1);

        //BUG FIX: Last enemy before the boss
        if (w.getLevel() == 4 && w.getEnemiesLeft() == 0 && isShooting) bullet.Destruct();

        //Spawn portal if no enemies remain or boss on last level
        if (w.getEnemiesLeft() == 0) {
            if (w.getLevel() < 4) {
                w.setLevelCompleted(true);
                if (w.getLevel() < 3) w.setPortal(new Portal(getWorld(), w.getLevel()));
                else if (w.getLevel() == 3) w.setPortal(new Portal(getWorld(), w.getLevel()));
            } else if (w.getLevel() == 4) {
                p.setEnemyBoss(new EnemyBoss(getWorld(), p,32));
                LevelManager.getGame().getControlPanel().BossDisplay();
            }
        }

        //Destroy the body
        destroy();

        //Award score for defeating an enemy
        if (enemyType == 1.1f) Player.setScore(Player.getScore() + 100);
        else if (enemyType == 1.2f) Player.setScore(Player.getScore() + 200);
        else if (enemyType == 1.3f) Player.setScore(Player.getScore() + 300);
        else if (enemyType == 2.1f) Player.setScore(Player.getScore() + 200);
        else if (enemyType == 2.2f) Player.setScore(Player.getScore() + 300);
        else if (enemyType == 2.3f) Player.setScore(Player.getScore() + 400);
        else if (enemyType == 3.1f) Player.setScore(Player.getScore() + 300);
        else if (enemyType == 3.2f) Player.setScore(Player.getScore() + 400);
        else if (enemyType == 3.3f) Player.setScore(Player.getScore() + 500);

        //By chance drop a pickup
        int rand = (int)(Math.random() * 10) + 1;
        if (rand == 9) new UpScore(getWorld());
        else if (rand >= 3 && rand <= 8) new UpCoin(getWorld());

        //Play explosion animation
        new EnemyExplode(this, 2);

        //Debug outputs
        System.out.println("Enemy: ("+iPos+", "+jPos+") has been destroyed!");
        System.out.println("There are " + w.getEnemiesLeft()+" enemies left");
        System.out.println("Score: " + Player.getScore());
    }

    /**
     * Moves enemy left to right. (Only enemies of type 2 and 3.
     * <p>
     *     This method generates {@link StepEvent} every frame.
     * </p>
     * @param stepEvent {@link StepEvent}.
     */
    @Override
    public void preStep(StepEvent stepEvent) {
        //Certain enemies can move left and right
        if ((int) enemyType == 2 || (int) enemyType == 3) {
            if (isMovingRight && !spawning) setLinearVelocity(new Vec2(enemyType*1.5f, 0));
            if (isMovingLeft && !spawning) setLinearVelocity(new Vec2(-enemyType*1.5f, 0));
        } if (getPosition().x >= iniX + 1.5f) {
            isMovingRight = false;
            isMovingLeft = true;
        } else if (getPosition().x <= iniX - 1.5f) {
            isMovingRight = true;
            isMovingLeft = false;
        }

        //Bug fix (enemy would shoot when loading from savedata)
        if (spawning && isShooting) bullet.Destruct();
    }
    /**
     * Moves enemy into position while spawning.
     * <p>
     *     This method generates {@link StepEvent} every frame.
     * </p>
     * @param stepEvent {@link StepEvent}.
     */
    @Override
    public void postStep(StepEvent stepEvent) {
        //Spawn animation for restricted amount of distance
        if (spawning && getPosition().y < iniY - 18.5f) {
            setLinearVelocity(new Vec2(0, 0));
            spawning = false;
            w.setEnemiesSpawning(false);
        }
    }

    /**
     * Method runs when enemy body collides with another body.
     * <p>
     *     This method generates a {@link CollisionEvent} every time {@link Enemy} collides with another {@link Body}.
     * </p>
     * @param e {@link CollisionEvent}.
     */
    @Override
    public void collide(CollisionEvent e) {
        //Remove a life if the enemy is hit by the player's bullet, destroys if 0
        setLinearVelocity(new Vec2(0, 0));
        if (e.getOtherBody() instanceof PlayerBullet) {
            float d = getPosition().x - p.getPosition().x;
            ((PlayerBullet) e.getOtherBody()).Destruct();
            lives--;
            if (lives == 0) {
                Destroy();

                //Ensures enemy on top shoots (Bug)
                if (iPos != 0 && enemyList[iPos-1][jPos] != null && !enemyList[iPos-1][jPos].isShooting && (d >= -2 && d <= 2))
                    enemyList[iPos-1][jPos].Shoot();
            }
            //Ensures that enemy shoots back (Bug)
            else if ((d >= -2 && d <= 2 && lives > 0) && !isShooting) Shoot();
        }

        UpdateSprite();
        System.out.println("Enemy ("+iPos+", "+jPos+") is hit!");
    }

    /**
     * Returns enemy type.
     * @return int value.
     */
    public float getEnemyType() {
        return enemyType;
    }

    /**
     * Returns true if the enemy is shooting.
     * @return true/false.
     */
    public boolean isShooting() {
        return isShooting;
    }

    /**
     * Set true/false for enemy shooting.
     * @param shooting true/false.
     */
    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    /**
     * Set value for enemy lives.
     * @param lives int value.
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Returns value of enemy lives.
     * @return int value.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns Body of enemy's bullet.
     * @return int value.
     */
    public EnemyBullet getBullet() {
        return bullet;
    }
}
