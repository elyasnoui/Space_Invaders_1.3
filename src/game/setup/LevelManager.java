package game.setup;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.objects.*;
import org.jbox2d.common.Vec2;

import javax.xml.crypto.NoSuchMechanismException;
import java.awt.*;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public abstract class LevelManager extends World implements Serializable {
    private static Game game;
    private Timer timer;
    private TimerTask task;
    private Player player = new Player(this);
    private Enemy[][] enemies;
    private int enemiesLeft;
    private boolean enemiesSpawning = true;
    private int level;
    private boolean levelCompleted = false;
    private Portal portal;
    private int secondsElapsed = 0;
    private int timeRemaining = 240;
    private boolean bossInGame = false;
    private static boolean isMusicMuted = false;
    private static boolean isSfxMuted = false;
    private static double musicVolume = 1;
    private static double sfxVolume = 1;
    private boolean switched = false;
    /**
     * Template world for all levels. Everything in this class is shared among levels.
     * @param level Current level player is at.
     */
    public LevelManager(int level) {
        super(60);
        this.level = level;

        //Extra time for level 4, because of boss battle
        if (level == 4) timeRemaining = 300;

        setGravity(0);
    }

    /**
     * Populate level with borders and firing zones.
     * @param g
     */
    public void Populate(Game g) {
        game = g;

        //Invisible borders
        Shape topS = new BoxShape(30f, 0.5f, new Vec2(0, 21.78f));
        Body topB = new StaticBody(this);
        topB.setName("Top");
        InvisibleBorder topF = new InvisibleBorder(topB, topS);

        Shape baseS = new BoxShape(30f, 0.5f, new Vec2(0, - 21.78f));
        Body baseB = new StaticBody(this);
        baseB.setName("Bottom");
        InvisibleBorder baseF = new InvisibleBorder(baseB, baseS);

        //Firing Zones
        int xPos = -16;
        for (int i=0; i<5; i++) {
            Shape s = new BoxShape(0.5f, 1, new Vec2(xPos,-18));
            Body b = new StaticBody(this);
            new FireZone(xPos, b, s, level);
            xPos+=8;
        }
    }

    /**
     * This method pauses the game.
     * <p>
     *     If parameter = 1: Pause and bring up the shop.
     *     Else: Only pause.
     * </p>
     * @param type int value
     */
    public void PauseLevel(int type) {
        //Pause level
        game.Pause();
        task.cancel();
        if (!LevelManager.isIsMusicMuted()) PauseMusic();
        if (type == 1) game.getShop().getMainPanel().setVisible(true);
        if (Player.getLives() < 3 && Player.getCoins() >= 10) game.getShop().getLifeUp().setEnabled(true);
        else game.getShop().getLifeUp().setEnabled(false);
        Game.frame.pack();
    }

    /**
     * This method resumes the game.
     * <p>
     *     Typical use of this is resuming the game after {@link LevelManager#PauseLevel(int)} is run.
     * </p>
     */
    public void ResumeLevel() {
        //Resume level
        game.Resume();
        SecondsElapsed();
        if (!LevelManager.isIsMusicMuted()) PlayMusic();
        game.getShop().getMainPanel().setVisible(false);
        Game.frame.pack();
    }

    /**
     * Method gets overriden by methods in specific levels. See:
     * <p>
     *     {@link Level1#PauseMusic()}
     *     </br>
     *     {@link Level2#PauseMusic()}
     *     </br>
     *     {@link Level3#PauseMusic()}
     *     </br>
     *     {@link Level4#PauseMusic()}
     * </p>
     */
    //Methods in level classes override these
    public void PauseMusic() {
    }

    /**
     * Method gets overriden by methods in specific levels. See PlayMusic() in level classes.
     */
    public void PlayMusic() {
    }

    /**
     * Method gets overriden by methods in specific levels. See ChangeMusicVolume() in level classes.
     * @param volume
     */
    public void ChangeMusicVolume(double volume) {
    }

    /**
     * Timer that tracks seconds elapsed and time remaining for each level.
     */
    public void SecondsElapsed() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                if (!isLevelCompleted()) {
                    timeRemaining--;

                    if (timeRemaining == 0) {
                        player.Destruct();
                        timeRemaining = 30;
                    }

                    //Play audio when running out of time
                    if (timeRemaining <= 30) {
                        if (!switched) {
                            System.out.println("change");
                            switch (level) {
                                case 1:
                                    ((Level1) getPlayer().getWorld()).ChangeMusic("data/audio/level1Fast.wav");
                                    break;
                                case 2:
                                    ((Level2) getPlayer().getWorld()).ChangeMusic("data/audio/level2Fast.wav");
                                    break;
                                case 3:
                                    ((Level3) getPlayer().getWorld()).ChangeMusic("data/audio/level3Fast.wav");
                                    break;
                                case 4:
                                    ((Level4) getPlayer().getWorld()).ChangeMusic("data/audio/level4Fast.wav");
                                    break;
                            } switched = true;
                        }
                    }
                }
            }
        }; timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * @return Returns Player object from World.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player Set Player object for World.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return Returns 2D Array holding current enemies in World.
     */
    public Enemy[][] getEnemies() {
        return enemies;
    }

    /**
     * @param enemies Set a 2D Array holding the enemies for World.
     */
    public void setEnemies(Enemy[][] enemies) {
        this.enemies = enemies;
    }

    /**
     * @return Returns value of enemies left in World.
     */
    public int getEnemiesLeft() {
        return enemiesLeft;
    }

    /**
     * @param enemiesLeft Set value for enemies left in World.
     */
    public void setEnemiesLeft(int enemiesLeft) {
        this.enemiesLeft = enemiesLeft;
    }

    /**
     * User cannot control player while isEnemiesSpawning is true.
     * @return Returns true if enemies are currently spawning.
     */
    public boolean isEnemiesSpawning() {
        return enemiesSpawning;
    }

    /**
     * Automatically sets to false when enemies spawn into designated positions.
     * @param enemiesSpawning Set true/false for enemies spawning.
     */
    public void setEnemiesSpawning(boolean enemiesSpawning) {
        this.enemiesSpawning = enemiesSpawning;
    }

    /**
     * @return Returns the Game user is in.
     */
    public static Game getGame() {
        return game;
    }

    /**
     * @return Returns level player is currently on.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Value becomes true when all enemies in World are completed.
     * If true, player can move both horizontally and vertically, but cannot shoot.
     * Portal spawns automatically when value is true.
     * @return Returns trie
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    /**
     * Automatically sets to true when enemiesLeft value is 0.
     * @param levelCompleted
     */
    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    /**
     * @return Returns Portal object in World.
     */
    public Portal getPortal() {
        return portal;
    }

    /**
     * @param portal Set Portal object.
     */
    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    /**
     * Decrements by 1 every second.
     * @param timeRemaining Set value for time remaining in World.
     */
    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    /**
     * When timeRemaining reaches 0, resets to 30 if Player still has extra lives, but decrements a life from Player.
     * @return
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Records the amount of seconds has elapsed since World has started.
     * @return Returns value of how many seconds has elapsed since starting level.
     */
    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    /**
     * @return Returns TimerTask that keeps track of secondsElapsed and timeRemaining.
     */
    public TimerTask getTask() {
        return task;
    }

    /**
     * Only true if it's Level 4 and there are no enemies but boss hasn't been defeated.
     * @return Returns true if the boss is in the game.
     */
    public boolean isBossInGame() {
        return bossInGame;
    }

    /**
     * @param bossInGame Set true/false for bossInGame.
     */
    public void setBossInGame(boolean bossInGame) {
        this.bossInGame = bossInGame;
    }

    /**
     * Level music stops playing when true.
     * @return Returns true if level music is currently muted.
     */
    public static boolean isIsMusicMuted() {
        return isMusicMuted;
    }

    /**
     * Method is called when mute is enabled in Options Panel.
     * @param isMusicMuted Set true/false for isMusicMuted.
     */
    public static void setIsMusicMuted(boolean isMusicMuted) {
        LevelManager.isMusicMuted = isMusicMuted;
    }

    /**
     * Level sfx stops playing when true.
     * @return Returns true if level sfx is currently muted.
     */
    public static boolean isIsSfxMuted() {
        return isSfxMuted;
    }

    /**
     * Method is called when mute is enabled in Options Panel.
     * @param isSfxMuted Set true/false for isSfxMuted.
     */
    public static void setIsSfxMuted(boolean isSfxMuted) {
        LevelManager.isSfxMuted = isSfxMuted;
    }

    /**
     * Method is called when music volume is toggled in Options Panel.
     * @return Returns value of music volume.
     */
    public static double getMusicVolume() {
        return musicVolume;
    }

    /**
     * @param musicVolume Returns value of current music volume.
     */
    public static void setMusicVolume(double musicVolume) {
        LevelManager.musicVolume = musicVolume;
    }

    /**
     * Method is called when sfx volume is toggled in Options Panel.
     * @return Returns value of sfx volume.
     */
    public static double getSfxVolume() {
        return sfxVolume;
    }

    /**
     * @param sfxVolume Returns value of current sfx volume.
     */
    public static void setSfxVolume(double sfxVolume) {
        LevelManager.sfxVolume = sfxVolume;
    }
}