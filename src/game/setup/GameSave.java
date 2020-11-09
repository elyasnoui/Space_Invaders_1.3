package game.setup;

import game.objects.Enemy;
import game.objects.EnemyBoss;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Elyas, Noui, elyas.noui@city.ac.uk
 * @version Java SE 13.0.2
 */
public class GameSave implements Serializable {
    private int level;
    private boolean isLevelCompleted;
    private boolean isBossInGame;
    private int bossHealth;
    private Vec2 playerPos;
    private int lives;
    private int score;
    private int bulletSpeed;
    private int coins;
    private int timeRemaining;
    private boolean isMusicMuted;
    private boolean isSfxMuted;
    private double musicVolume;
    private double sfxVolume;
    private String[][] enemiesNull;
    private int[][] enemiesHealth;
    public GameSave(
            int level, boolean isLevelCompleted, boolean isBossInGame, Vec2 playerPos, int lives, int score,
            int bulletSpeed, int coins, int timeRemaining, boolean isMusicMuted, boolean isSfxMuted, double musicVolume,
            double sfxVolume, Enemy[][] enemies) {

        //All properties needed
        this.level = level;
        this.isLevelCompleted = isLevelCompleted;
        this.isBossInGame = isBossInGame;
        this.playerPos = playerPos;
        this.lives = lives;
        this.score = score;
        this.bulletSpeed = bulletSpeed;
        this.coins = coins;
        this.timeRemaining = timeRemaining;
        this.isMusicMuted = isMusicMuted;
        this.isSfxMuted = isSfxMuted;
        this.musicVolume = musicVolume;
        this.sfxVolume = sfxVolume;

        if (isBossInGame) bossHealth = EnemyBoss.getLives();

        //Record enemy states into arrays
        if (level < 4) {
            enemiesNull = new String[5][5];
            enemiesHealth = new int[5][5];
            for (int i=0; i<5; i++) {
                for (int j=0; j<5; j++) {
                    if (enemies[i][j] == null) enemiesNull[i][j] = "destroyed";
                    else enemiesHealth[i][j] = enemies[i][j].getLives();
                }
            }
        } else {
            enemiesNull = new String[3][5];
            enemiesHealth = new int[3][5];
            for (int i=0; i<3; i++) {
                for (int j=0; j<5; j++) {
                    if (enemies[i][j] == null) enemiesNull[i][j] = "destroyed";
                    else enemiesHealth[i][j] = enemies[i][j].getLives();
                }
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public boolean isLevelCompleted() {
        return isLevelCompleted;
    }

    public boolean isBossInGame() {
        return isBossInGame;
    }

    public int getBossHealth() {
        return bossHealth;
    }

    public Vec2 getPlayerPos() {
        return playerPos;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public int getCoins() {
        return coins;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public boolean isMusicMuted() {
        return isMusicMuted;
    }

    public boolean isSfxMuted() {
        return isSfxMuted;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public String[][] getEnemiesNull() {
        return enemiesNull;
    }

    public int[][] getEnemiesHealth() {
        return enemiesHealth;
    }
}
