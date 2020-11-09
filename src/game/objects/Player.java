package game.objects;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.setup.Audio;
import game.setup.LevelManager;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class Player extends Walker implements CollisionListener, Serializable {
    private final Shape topShape = new PolygonShape(0.341f,0.311f, -0.334f,0.311f, -0.341f,0.98f, 0.341f,0.986f);
    private final Shape bottomShape = new PolygonShape(1.479f,-0.833f, -1.472f,-0.826f, -1.472f,0.299f, 1.485f,0.311f);
    private final Fixture topFixture = new SolidFixture(this, topShape);
    private final Fixture bottomFixture = new SolidFixture(this, bottomShape);
    private final BodyImage image = new BodyImage("data/player/player.png", 2.5f);

    //These static fields are intended to be held throughout all levels
    /**
     * Keeps track of number of lives the player has.
     */
    private static int lives = 3;
    /**
     * Keeps track of the score.
     */
    private static int score = 0;
    /**
     * Keeps track of the bullet's speed.
     */
    private static int bulletSpeed = 15;
    /**
     * Keeps track of the amount of coins Player has.
     */
    private static int coins = 0;

    private boolean spawning = false;
    private boolean isShooting = false;
    private Enemy[][] enemiesList;
    private EnemyBoss enemyBoss;
    /**
     * Creates an instance of player, the user controls the player, players can move and shoot.
     * <p>
     *     This method adds a collision listener to the Player's Body and attaches a BodyImage to the player.
     *     After this is done it will spawn the player into the scene using {@link game.objects.Player#Spawn(float)}
     * </p>
     */
    public Player(World w) {
        super(w);

        addImage(image);
        addCollisionListener(this);

        //Spawn the player
        Spawn(0);
    }

    /**
     * Spawns the {@link game.objects.Player} into the scene.
     * @param xPos is used to spawn the player in whatever x position it's currently at.
     */
    //Separate method is needed for player to spawn and respawn (See Usages)
    public void Spawn(float xPos) {
        spawning = true;
        setPosition(new Vec2(xPos, -23));
        setLinearVelocity(new Vec2(0, 5));
    }

    /**
     * Destroys the {@link game.objects.Player}'s body and decrements {@link game.objects.Player#lives}.
     * If the player has no more lives, returns game state to Main Menu
     */
    public void Destruct() {
        //Take away 1 life point and respawn
        lives--;
        Spawn(getPosition().x);
        System.out.println("Player hit, lives left: " + lives);

        //Play death sound effect
        if (!LevelManager.isIsSfxMuted())
            new Audio("data/audio/playerDeath.wav", "play", "sfx");

        //Return to the main menu if game over
        if (lives == 0) LevelManager.getGame().DisplayMenu();
    }

    /**
     * Returns true if the player is currently spawning.
     * <p>
     *     Typical use of this method will be in {@link game.setup.Controller} when restricting movement.
     *     Another place it is used is by {@link game.objects.Enemy} to restrict shooting while spawning.
     * </p>
     * @return true/false
     */
    public boolean isSpawning() {
        return spawning;
    }

    /**
     * Set true/false for player spawning.
     * <p>
     *     This will typically be caused by loading the game and setting the player in a previous position in {@link game.gui.Options}
     * </p>
     * @param spawning
     */
    public void setSpawning(boolean spawning) {
        this.spawning = spawning;
    }

    /**
     * Returns true if the player is currently shooting.
     * <p>
     *     This method is typically used by {@link game.setup.Controller} to restrict/allow {@link Player}
     *     to shoot an instance of {@link PlayerBullet}.
     * </p>
     * @return true/false
     */
    public boolean isShooting() {
        return isShooting;
    }

    /**
     * Set true/false for player shooting.
     * <p>
     *     This is used in {@link PlayerBullet#collide(CollisionEvent)} when a bullet collides with another entity and is destroyed.
     *     {@link Player} can only shoot one bullet at a time.
     * </p>
     * @param shooting boolean value.
     */
    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    /**
     * Set value for player lives.
     * <p>
     *     This is used when {@link Player} collides with an instance of {@link EnemyBullet} or
     *     {@link BossBullet} as colliding with these causes {@link Player} to destroy.
     *     The destroy method can be found here {@link Player#Destruct()}.
     * </p>
     * @param lives int value.
     */
    public static void setLives(int lives) {
        Player.lives = lives;
    }

    /**
     * Returns how many lives the player has left.
     * <p>
     *     Typical use of this will be in {@link game.gui.MyView#paintForeground(Graphics2D)} when displaying the
     *     HUD showing how many {@link Player#lives} are left.
     *     </br>
     *     Another place it's used is in {@link Player#Destruct()} to determine whether to call {@link Player#Spawn(float)}
     *     or to return to {@link game.gui.MainMenu}.
     * </p>
     * @return int value.
     */
    public static int getLives() {
        return lives;
    }

    /**
     * Returns current array of enemies the player is facing.
     * @return 2D Array of type {@link Enemy}
     */
    public Enemy[][] getEnemiesList() {
        return enemiesList;
    }

    /**
     * Set an array of enemies that player is facing.
     * @param enemiesLeft 2D Array of type {@link Enemy}
     */
    public void setEnemiesList(Enemy[][] enemiesLeft) {
        this.enemiesList = enemiesLeft;
    }

    /**
     * @return Returns the boss's body.
     */
    public EnemyBoss getEnemyBoss() {
        return enemyBoss;
    }

    /**
     * @param enemyBoss Set a body for the boss.
     */
    public void setEnemyBoss(EnemyBoss enemyBoss) {
        this.enemyBoss = enemyBoss;
    }

    /**
     * Method can be accessed without Player object.
     * @return Returns value of current score player has.
     */
    public static int getScore() {
        return score;
    }

    /**
     * Method can be accessed without Player object.
     * @param score Set value for player score.
     */
    public static void setScore(int score) {
        Player.score = score;
    }

    /**
     * Method can be accessed without Player object.
     * @return Returns value of current speed bullets go at from player.
     */
    public static int getBulletSpeed() {
        return bulletSpeed;
    }

    /**
     * Method can be accessed without Player object.
     * @param bulletSpeed Set value for bullet speed.
     */
    public static void setBulletSpeed(int bulletSpeed) {
        Player.bulletSpeed = bulletSpeed;
    }

    /**
     * Method can be accessed without Player object.
     * @return Returns value of coins collected.
     */
    public static int getCoins() {
        return coins;
    }

    /**
     * @param coins Set value of coins collected.
     */
    public static void setCoins(int coins) {
        Player.coins = coins;
    }

    /**
     * Method runs when {@link Player}'s collides with another {@link Body} in {@link World}.
     * <p>
     *     This method will check what type of {@link Body} has been hit, in which it will determine
     *     course of action. e.g. Hit by a bullet > decrement life.
     * </p>
     * @param e CollisionEvent generated, has access to reporting and other Bodies.
     */
    @Override
    public void collide(CollisionEvent e) {
        setLinearVelocity(new Vec2(0,0));

        //Special case for when all enemies have been destroyed
        if (((LevelManager) getWorld()).isLevelCompleted()) {
            setPosition(new Vec2(getPosition().x, getPosition().y));

            if (e.getOtherBody() instanceof Portal) {
                ((Portal) e.getOtherBody()).Teleport(this);
            }
        }
        else {
            //Ensure collisions dont cause player to offset during level
            setPosition(new Vec2(getPosition().x, -18));

            //If player collides with an enemy's bullet
            if (e.getOtherBody() instanceof EnemyBullet || e.getOtherBody() instanceof BossBullet) {
                if (e.getOtherBody() instanceof EnemyBullet) ((EnemyBullet) e.getOtherBody()).Destruct();
                else if (e.getOtherBody() instanceof BossBullet) ((BossBullet) e.getOtherBody()).Destruct();
                Destruct();
            }
        }
    }
}
